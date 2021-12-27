import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Queue;
import java.util.PriorityQueue;

public class AStarDiv_912141552 implements AIModule
{
    class Index {
        private ArrayList<Point> PathArray;
        private Point Point;
        private double Cost;
        private double FullCost;

        public Index(Point Point){
            this.PathArray = new ArrayList<Point>();
            this.Point = new Point(Point);
            this.Cost = 0.0;
            this.FullCost = 0.0;
        }
        
        public Point getPoint() {
            return Point;
        }
        public ArrayList<Point> getPath() {
            return PathArray;
        }
        public double getCost() {
            return Cost;
        }
        public void setPathArray (ArrayList<Point> PathArray){
            this.PathArray = (ArrayList)PathArray;
        }
        public void setCost (double Cost){
            this.Cost = Cost;
        }
        public void setFullCost(double FullCost){
            this.FullCost = FullCost;
        }
    }

    private double getHeuristic (final TerrainMap map, final Point pt1, final Point pt2)
    {
        int xDiff = pt1.x - pt2.x;
        int yDiff = pt1.y - pt2.y;
        double PointToPoint = map.getCost(pt1,pt2);
        double MultAdd = xDiff*xDiff + yDiff*yDiff;
        double Euclidean = Math.sqrt(MultAdd);
        double TotalCost = PointToPoint + Euclidean;
        return TotalCost;
    }

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        // Holds the resulting path
        final ArrayList<Point> path = new ArrayList<Point>();

        // Store the Start point, end point, and first cost
        final Point CurrentPoint = map.getStartPoint();
        final Point EndPoint = map.getEndPoint();
        double FirstFullCost = getHeuristic(map, CurrentPoint,EndPoint);

        // Compares the index of the points and with index of the cost
        class PointCompare implements Comparator<Index>{
            public int compare(Index p1, Index p2){
                return (int) (p1.FullCost-p2.FullCost);
            }
        }
        // Create the Open and Closed List
        Queue<Index> OpenList = new PriorityQueue<>(new PointCompare());
        ArrayList<Point> ClosedList = new ArrayList<Point>(); 
        int ClosedSize = ClosedList.size();

        // Initialize the Queue with the first Point
        Index FirstIndex = new Index(CurrentPoint);

        // PathArray stores the the path
        ArrayList<Point> PathArray = (ArrayList)FirstIndex.getPath();
        PathArray.add(new Point(CurrentPoint));
        FirstIndex.setFullCost(FirstFullCost);
        FirstIndex.setPathArray(PathArray);
        OpenList.offer(FirstIndex);
        
        //Loops until we find the end point
        while (true){

            Index CurrentPath = OpenList.poll(); // Gets the current path that we are working on
            Point NewCurrentPoint = CurrentPath.getPoint(); // Gets the point that we are working on
            ClosedList.add(new Point(NewCurrentPoint)); // Add the new point to the closed list

            Point[] NeighborArray = map.getNeighbors(NewCurrentPoint);
            int NeighborSize = NeighborArray.length;
            Point NewNeighbor;
            Point ThisPoint;
            for(int i = 0; i < NeighborSize; i++){ // check if neighbors in neighborsList
                NewNeighbor = NeighborArray[i];
                int Counter = 0;
                for(int j = 0; j < ClosedSize; j++){
                    ThisPoint = ClosedList.get(j);
                    if(NewNeighbor.x == ThisPoint.x && NewNeighbor.y == ThisPoint.y){
                        Counter = 1;
                        break;
                    }
                }
                // When we find a new neighbor
                if (Counter == 0){ 
                    ArrayList<Point> CreatePath = (ArrayList)CurrentPath.getPath().clone();
                    CreatePath.add(new Point(NewNeighbor));
                    double CurrentCost = CurrentPath.getCost();
                    double neighborCost = map.getCost(NewCurrentPoint, NewNeighbor);
                    double OldFullCost = CurrentCost + neighborCost;
                    double FullCost = OldFullCost + getHeuristic(map, NewNeighbor,EndPoint);
                    Index NextIndex = new Index(NewNeighbor);
                    NextIndex.setFullCost(FullCost);
                    NextIndex.setCost(OldFullCost);
                    NextIndex.setPathArray(CreatePath);
                    OpenList.offer(NextIndex);
                }
            }
            if (NewCurrentPoint.x == EndPoint.x && NewCurrentPoint.y == EndPoint.y){
                ArrayList<Point> FinalPath = CurrentPath.getPath();
                int FinalSize = FinalPath.size();
                for(int i = 0; i < FinalSize; i++)
                {
                    path.add(new Point(FinalPath.get(i)));
                }
                // We're done!  Hand it back.
                return path;
            }
        }
    }

}