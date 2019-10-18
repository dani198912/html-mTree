
import java.util.*;



public class Main {
    public static void main(String[] args) {
        // Test string map...
        String[] html = {"<body>", "<tagn class = \"ciuciu\" data-struct = \"shitty data\">", "</tagn>", "<div>", "</div>", "<div>", "<tag1>","<br>", "<p>", "</p>", "</tag1>", "<p>", "</p>", "<p>", "</p>", "<p>", "</p>", "</div>", "<div>", "<p>", "</p>", "<p>", "</p>", "</div>", "</body>"};
        ArrayList<Node> nodeList = new ArrayList<Node>();//LIST CREATED FROM THE STRING LIST
        ArrayList<Node> visiting = new ArrayList<Node>();//VISITING IS ONLY A TEMP LIST
        for (String s : html) {
            nodeList.add(new Node(s));
        }
        if (nodeList.get(0).isRoot()) {
            visiting.add(nodeList.get(0));
        }
        //for all nodes in the nodesList created earlier
        for (int i = 0; i < nodeList.size() - 1; i++) {
            Node currentNode = nodeList.get(i);
            Node nextNode = nodeList.get(i + 1);
            Node lastVisited = visiting.get(visiting.size() - 1);

            if (currentNode.getIsEmptyTag()) {
                lastVisited.addChild(currentNode);
                continue;
            }
            if(!currentNode.IsEndTag() && !nextNode.IsEndTag() && !visiting.contains(currentNode))
            {
                lastVisited.addChild(currentNode);
                visiting.add(currentNode);
            }
            if(!currentNode.IsEndTag() && nextNode.IsEndTag() && !visiting.contains(currentNode))
            {
                visiting.add(currentNode);
                lastVisited.addChild(currentNode);
            }
            if(lastVisited.isNodeEndTag(currentNode)){
                visiting.remove(visiting.size()-1);
            }
        }
        Node root = nodeList.get(0);

        for (Node x: root.inOrderView){
            System.out.println(x.getAttribute("data-struct"));
        }
        System.out.println(root.inOrderView.getClass());
    }


    //OLD InOrder View
//    public static ArrayList<Node> treeList (Node root){
//        ArrayList<Node> list = new ArrayList<Node>();
//        preOrder(root, list);
//        return list;
//    }
//
//    public static void preOrder(Node root, ArrayList<Node> listOfNodes)
//    {
//        if(root == null) return;
//        listOfNodes.add(root);
//        if(!root.isLeaf())
//        {
//            for (Node c:root.getAllChild()){
//                preOrder(c, listOfNodes);
//            }
//        }
//
//    }

}

class Node{
    private String tagName;
    private Node parent;
    private ArrayList<Node> child = new ArrayList<Node>();
    private boolean isEmptyTag;// empty tags are self closing tags
    private HashMap<String, String> attributesMap = new HashMap<String, String>();
    Node()
    {
        tagName = "";
        parent = null;
        child = new ArrayList<Node>();
    }

    Node(String varData)
    {
        this.attributesMap = processAttributes(varData);
        this.tagName = attributesMap.get("tagName");
        this.isEmptyTag = checkEmptyTag();
    }
    public void setParent(Node parentNode)
    {
        this.parent = parentNode;
    }
    //ADD Single node
    public void addChild(Node childNode)
    {
        if(!this.child.contains(childNode) && !this.child.contains(this))
        {
            childNode.setParent(this);
            this.child.add(childNode);
        }
    }
    //ADD a List of nodes
    public void addChild(ArrayList<Node> multNodes)
    {
        for (Node nodes:multNodes) {
            if (!this.child.contains(nodes) && !this.child.contains(this))
            {
                nodes.setParent(this);
                this.child.add(nodes);
            }
        }
    }
    public boolean getIsEmptyTag()
    {
        return this.isEmptyTag;
    }

    public String getTagName()
    {
        return this.tagName;
    }

    public Node getFirstChild()
    {
        if(!this.isLeaf())
        {
            return this.child.get(0);
        } else
        {
            return null;
        }
    }

    public void deleteFirstChild(){
        if(!this.isLeaf())
        this.child.remove(0);
    }

    public Node getLastChild()
    {
        if(!this.isLeaf()){
            return this.child.get(this.child.size()-1);
        } else {
            return null;
        }
    }

    public void deleteLastChild()
    {
        if(!this.isLeaf())
        this.child.remove(this.child.size()-1);
    }

    public ArrayList<Node> getAllChild()
    {
        return this.child;
    }

    public Node getParent()
    {
        return this.parent;
    }

    public boolean isRoot()
    {
        return (this.parent == null);
    }

    public boolean isLeaf()
    {
        return (this.child.size()==0);
    }

    public boolean IsEndTag()
    {
        return tagName.contains("/");
    }

    //Get the name of an endTag element without the / character
    //Useful to see if visited node is the last visited nodes closing element
    public String getEndTagName()
    {
        if(this.tagName.contains("/"))
        {
            return this.tagName.replaceAll("/", "");
        }
        else{
            return this.tagName;
        }
    }

    //check if the tag is a closing tag. A closing tag is a tag which contains the '/' character like: </someTag>
    public boolean isNodeEndTag(Node n1){
        if(n1.IsEndTag() && (this.tagName.equals(n1.getEndTagName())))
        {
            return true;
        } else {
            return false;
        }
    }

    //get cleared by <,>,/ tagName
    public String cleanTagName(){
        return this.tagName.replaceAll("<","").replaceAll(">","").replaceAll("/","");
    }

    //check if the tag is an empty tag or so called self closing tag
    public boolean checkEmptyTag()
    {
        String[] EMPTY_TAGS = {"br", "area", "base", "col", "embed", "hr", "input", "link", "meta", "param", "source", "wbr"};
        for (String eTag:EMPTY_TAGS)
        {
            if(this.cleanTagName().contains(eTag.replaceAll("<","").replaceAll(">","").replaceAll("/","")))
            {
                return true;
            }
        }
        return false;
    }

    //Cap2. Populate the attributes map to be able to acces it
    public HashMap<String, String> processAttributes(String s){
        HashMap<String, String> attrMap = new HashMap<String, String>();

        //CLEARING AND GETTING THE TAG NAME AND END TAG NAME
        if(!s.contains("/")) {
            if(s.contains(" "))
            {
                String tagName = s.substring(s.indexOf("<"), s.indexOf(" ")) + ">";
                s = s.substring(s.indexOf(" "));
                s = s.replace(">", "");
                System.out.println("Resulting string is:" + s);
                attrMap.put("tagName", tagName);
            } else {
                attrMap.put("tagName", s.replaceAll(" ", ""));
            }

        } else {
            attrMap.put("tagName", s.replaceAll(" ", ""));
        }
        // GETTING THE ATTRIBUTES DONE
        while (!s.contains("/")&&(s.contains("=") || s.contains("\""))) {
            String attr = s.substring(0, s.indexOf("=")).trim();

            s = s.substring(s.indexOf("=") + 1);
            s = s.substring(s.indexOf("\"")+1);
            String attrData = s.substring(0, s.indexOf("\"")).trim();

            s = s.substring(s.indexOf("\"")+1);
            attrMap.put(attr, attrData);
        }
        return attrMap;
    }

    public String getAttribute(String attrName){
        if(this.attributesMap.containsKey(attrName)){
            return attributesMap.get(attrName);
        } else {
            return null;
        }
    }
    //Cap3.THE ITERATOR InOrder Acces of elements ... for later sorting in other trees.
    // TODO: 16-Oct-19 Make the Node class iterable with an enhanced for loop.
    static class InOrderIterator implements Iterator<Node> {
        final Queue<Node> queue = new LinkedList<Node>();

        public InOrderIterator(Node tree) {
            queue.add(tree);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Node next() {
            Node node = queue.remove();
            queue.addAll(node.getAllChild());
            return node;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    Iterable<Node> inOrderView = new Iterable<Node>() {
        @Override
        public Iterator<Node> iterator() {
            return new InOrderIterator(Node.this);
        }
    };
    //Cap3. End


}


