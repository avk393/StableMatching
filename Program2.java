import java.util.ArrayList;

/**
 * Created by tkalbar on 3/2/19.
 */

public class Program2 {

    private class IntensityData {
        int weight;
        int edge_weight;
        int row;
        int col;
        boolean visited;

        IntensityData(int weight, int x, int y) {
            this.weight = weight;
            row = x;
            col = y;
            visited = false;
            edge_weight = 0;
        }

        int getWeight() { return weight; }
        int getEdgeWeight() { return edge_weight; }
        int getRow() { return row; }
        int getCol() { return col; }
        boolean hasVisited() { return visited; }

        void setEdgeWeight (int edge_weight) { this.edge_weight = edge_weight; }
        void setVisited() { this.visited = true; }
    }



    public int constructIntensityGraph(int[][] image){
        int total_weight = 0;

        for (int row=0; row<image.length; row++){
            for (int col=0; col<image[row].length; col++){
                // calculating weight w/ node right of current node
                if(col != (image[row].length-1)) { total_weight += Math.abs(image[row][col]-image[row][col+1]); }

                // calculating weight w/ node below current node
                if(row != (image.length-1)) { total_weight += Math.abs(image[row][col]-image[row+1][col]); }
            }
        }

        return total_weight;
    }

    public int constructPrunedGraph(int[][] image){
        int total_weight = 0;
        int total_size = 0;
        int row_size = image[0].length;
        ArrayList<IntensityData> pixels = new ArrayList<>();
        IntensityData[][] pixels_data = new IntensityData[image.length][row_size];

        // initializing data structure to hold weights
        for (int row=0; row<image.length; row++){
            for (int col=0; col<image[row].length; col++){
                IntensityData node = new IntensityData(image[row][col], row, col);
                pixels.add(node);
                pixels_data[row][col] = node;
                total_size++;
            }
        }

        // greedy part
        IntensityData key_node = pixels_data[0][0];     // starting point
        int counter = 1;
        ArrayList<IntensityData> heap = new ArrayList<>();
        // takes lowest edge weight adjacent to node
        while (counter<total_size) {
            // looking at adj nodes
            key_node.setVisited();
            IntensityData node = exploredAdjNode(pixels_data,heap,key_node);
            int edge = Math.abs(key_node.getWeight()-node.getWeight());
            // if path hits dead end or a shorter edge weight is available
            if(node==key_node || heap.get(0).getEdgeWeight()<edge) {
                // if node will not be taken but needs to update edge weight
                if (heap.contains(node) && node.getEdgeWeight()>edge) {
                    removeFromHeap(heap,heap.indexOf(node));
                    node.setEdgeWeight(edge);
                    heapifyUp(heap,node);
                }
                node = removeFromHeap(heap,0);
                if(node.hasVisited()) continue;
                edge = node.getEdgeWeight();
            }

            total_weight += edge;
            key_node = node;
            counter++;
        }


        return total_weight;
    }

    private IntensityData exploredAdjNode (IntensityData[][] pixels, ArrayList<IntensityData> heap, IntensityData key_node) {
        int edge_weight;
        int min_weight = 1000000;
        int row = key_node.getRow();
        int col = key_node.getCol();

        IntensityData node = key_node;
        // finding minimum edge weight adj to key_node
        if(row!=0) {
            if(pixels[row-1][col].hasVisited()==false) {
                edge_weight = Math.abs(key_node.getWeight()-pixels[row-1][col].getWeight());
                if (edge_weight < min_weight) {
                    min_weight = edge_weight;
                    node = pixels[row - 1][col];
                }
            }
        }
        if(row<(pixels.length-1)) {
            if(pixels[row+1][col].hasVisited()==false) {
                edge_weight = Math.abs(key_node.getWeight()-pixels[row+1][col].getWeight());
                if (edge_weight < min_weight) {
                    min_weight = edge_weight;
                    node = pixels[row+1][col];
                }
            }
        }
        if(col!=0) {
            if(pixels[row][col-1].hasVisited()==false) {
                edge_weight = Math.abs(key_node.getWeight()-pixels[row][col-1].getWeight());
                if (edge_weight < min_weight) {
                    min_weight = edge_weight;
                    node = pixels[row][col-1];
                }
            }
        }
        if(col<(pixels[0].length-1)) {
            if(pixels[row][col+1].hasVisited()==false) {
                edge_weight = Math.abs(key_node.getWeight()-pixels[row][col+1].getWeight());
                if (edge_weight < min_weight) {
                    node = pixels[row][col+1];
                }
            }
        }

        // adding all other qualified edges to heap
        if(row!=0) {
            if(pixels[row-1][col].hasVisited()==false && node!=pixels[row-1][col]){
                IntensityData point = pixels[row-1][col];
                edge_weight = Math.abs(key_node.getWeight()-point.getWeight());
                // if node is already in heap, check if current edge weight is less
                checkAddHeap(heap,point,edge_weight);
            }
        }
        if(row<(pixels.length-1)) {
            if(pixels[row+1][col].hasVisited()==false && node!=pixels[row+1][col]){
                IntensityData point = pixels[row+1][col];
                edge_weight = Math.abs(key_node.getWeight()-point.getWeight());
                // if node is already in heap, check if current edge weight is less
                checkAddHeap(heap,point,edge_weight);
            }
        }
        if(col!=0) {
            if(pixels[row][col-1].hasVisited()==false && node!=pixels[row][col-1]){
                IntensityData point = pixels[row][col-1];
                edge_weight = Math.abs(key_node.getWeight()-point.getWeight());
                // if node is already in heap, check if current edge weight is less
                checkAddHeap(heap,point,edge_weight);
            }
        }
        if(col<(pixels[0].length-1)) {
            if(pixels[row][col+1].hasVisited()==false && node!=pixels[row][col+1]){
                IntensityData point = pixels[row][col+1];
                edge_weight = Math.abs(key_node.getWeight()-point.getWeight());
                // if node is already in heap, check if current edge weight is less
                checkAddHeap(heap,point,edge_weight);
            }
        }

        return node;
    }

    private void checkAddHeap(ArrayList<IntensityData> heap, IntensityData point, int edge_weight) {
        // adding point to the heap
        // want to check if point is already in heap to update edge weight if necessary
        if(heap.contains(point)) {
            if(point.getEdgeWeight()>edge_weight) {
                point.setEdgeWeight(edge_weight);
                removeFromHeap(heap,heap.indexOf(point));
                heapifyUp(heap,point);
            }
        }
        else {
            point.setEdgeWeight(edge_weight);
            heapifyUp(heap, point);
        }
    }

    private void heapifyUp(ArrayList<IntensityData> heap, IntensityData node){
        if (node==null) return;

        heap.add(node);
        // until node gets to root of heap
        while(heap.indexOf(node)!=0){
            int parent;
            int index = heap.indexOf(node);
            // finding parent index
            if((index%2)==0) { parent = (index-2)/2; }
            else { parent = (index-1)/2; }

            // swapping with parent if necessary
            if(heap.get(parent).getEdgeWeight()>node.getEdgeWeight()){
                heap.set(index,heap.get(parent));
                heap.set(parent,node);
            }
            else break;
        }
    }

    private IntensityData removeFromHeap(ArrayList<IntensityData> heap, int index) {
        IntensityData data;
        if(heap==null || index>heap.size()-1) return null;
        else if(heap.size()==1) {
            data = heap.get(0);
            heap.remove(0);
            return data;
        }

        data = heap.get(index);
        heap.set(index,heap.get(heap.size()-1));
        heap.remove(heap.size()-1);
        // until we reach bottom of tree
        while((heap.size()-1) > (index*2)) {
            IntensityData node = heap.get(index);
            try {
                IntensityData child1 = heap.get((index * 2) + 1);
                IntensityData child2 = heap.get((index * 2) + 2);

                if (child1.getEdgeWeight() < node.getEdgeWeight()) {
                    int temp_index = heap.indexOf(child1);
                    heap.set(temp_index, node);
                    heap.set(index, child1);
                    index = temp_index;
                } else if (child2.getEdgeWeight() < node.getEdgeWeight()) {
                    int temp_index = heap.indexOf(child2);
                    heap.set(temp_index, node);
                    heap.set(index, child2);
                    index = temp_index;
                } else break;
            }
            // if there is only one child of node
            catch (IndexOutOfBoundsException e) {
                IntensityData child1 = heap.get((index * 2) + 1);

                if (child1.getEdgeWeight() < node.getEdgeWeight()) {
                    int temp_index = heap.indexOf(child1);
                    heap.set(temp_index, node);
                    heap.set(index, child1);
                    index = temp_index;
                } else break;
            }
        }

        return data;
    }

}
