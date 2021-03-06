
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author Lütfullah TÜRKER
 */
public abstract class AbstractGraphExtended extends AbstractGraph{
    
    public AbstractGraphExtended(int numV, boolean directed) {
        super(numV, directed);
    }
    /**
     * 
     * @param edgeLimit rasgele gelecek sayının üst sınırı
     * @return kaç tane edge eklendiyse o sayıyı return eder.
     * @throws Exception 
     */
    public int addRandomEdgesToGraph (int edgeLimit) throws Exception{
        if (edgeLimit < 0)
            throw new Exception("edgeLimit cannot be lower than 0 !");
        Random randGenerator = new Random();
        int randNum = randGenerator.nextInt(edgeLimit);
        int src,dest,exist = 0;
        for (int i = 0 ;i<randNum;++i){
            src = randGenerator.nextInt(super.getNumV());
            dest = randGenerator.nextInt(super.getNumV());
            if (!isEdge(src,dest))
                insert(new Edge(src,dest));
            else
                exist ++ ;
        }
        return randNum - exist;
    }
    
    /**
     * 
     * @param start Aramaya başlanılacak vertex indexi
     * @return 
     */
    public int [] breadthFirstSearch (int start){
        boolean[] visited = new boolean[super.getNumV()];
        Queue<Integer> queue = new LinkedList<>();
        int[] parent = new int[getNumV()];
        for (int i = 0; i < getNumV(); i++) {
            parent[i] = -1;
        }
        queue.add(start);
        while (queue.isEmpty() == false) {
            int val = queue.poll();
            visited[val] = true;
            Iterator<Edge> iter = edgeIterator(val);
            while (iter.hasNext()) {
                Edge nextEdge = iter.next();
                if (visited[nextEdge.getDest()] == false) {
                    queue.add(nextEdge.getDest());
                    visited[nextEdge.getDest()] = true;
                    parent[nextEdge.getDest()] = val;
                }
            }
        }
        return parent;
    }
    /**
     * breadthFirstSearch fonksiyonunu kullanarak bağlı grafları tespit ediyoruz.
     * -1 sayısı kadar bağlı graf vardır.Fonksiyonu tamamlamadım.
     * @return connectedComponentleri içeren graf arrayi döndürür.
     */
    public Graph [] getConnectedComponentUndirectedGraph (){
        if (super.isDirected())
            return null;
        int count1 = 0,rootCount = 0;
        int [] BFS = breadthFirstSearch(0);
        for (int i =0;i < BFS.length;++i)
            if (BFS[i] == -1)
                count1 ++;
        Graph[] graphArr = new Graph[count1];
        int [] roots = new int[count1];
        for (int i =0;i < BFS.length;++i)
            if (BFS[i] == -1)
                roots[rootCount++] = i;
        
        return null;
    }
    /**
     * Fonksiyon connectedComponent graflarda da çalışması için bir yardımcı fonksiyon
     * ile tüm grafı gezerek işlem yapıyorum.BFS işlemindeki algoritmayı kullanıyorum.
     * @return 
     */
    public boolean isBipartiteUndirectedGraph (){
        if (super.isDirected())
            return false;
        int [] setControlArr = new int[super.getNumV()];
        for (int i = 0; i < getNumV(); ++i)
            setControlArr[i] = -1;
        for (int i = 0; i < getNumV(); i++)
          if (setControlArr[i] == -1)
            if (isBipartiteHelper(i, setControlArr) == false)
               return false;
         return true;
    }
    /**
     * BFS algoritması ile gelen source 'ye göre algoritmayı uyguluyorum.
     * @param source
     * @param setControlArr Elemanların hangi kümeye ait olduğunu içeren array
     * @return 
     */
    private boolean isBipartiteHelper(int source,int[] setControlArr){
        setControlArr[source] = 1;
        Queue <Integer> queue = new LinkedList<>();
        queue.offer(source);
        while (!queue.isEmpty())
        {
            int u = queue.poll();
            for (int v = 0; v < getNumV(); ++v)
            {
                if (isEdge(u, v) && setControlArr[v] == -1)
                {
                    setControlArr[v] = 1 - setControlArr[u];
                    queue.offer(v);
                }
                else if (isEdge(u, v) && setControlArr[v] == setControlArr[u])
                    return false;
            }
        }
        return true; 
    }    
    /**
     * Grafı verilen isimdeki dosyaya yazar.Graf undirected ise örn. 1 0 var ise 0 1 i tekrar dosyaya yazmaz.
     * @param fileName  yazılacak dosya adı
     * @throws IOException Hata durumunda exception fırlatıyoruz.
     */
    public void writeGraphToFile (String fileName) throws IOException{
        
        try {
            File file = new File("Results/"+fileName);
            if (!file.exists())
                file.createNewFile();
            FileWriter fileWR = new FileWriter(file);
            fileWR.write(super.getNumV()+"\n");
            for (int i = 0;i < super.getNumV();++i)
                for ( int j = 0 ; j< super.getNumV();++j){
                    Edge edge = getEdge(i, j);
                    if (!(edge.getWeight() == Double.POSITIVE_INFINITY) && edge.getSource() <= edge.getDest())
                        fileWR.write(edge.getSource()+" "+edge.getDest()+"\n");
                }
            fileWR.close();
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Scanner Failed ");
        }
    }
}