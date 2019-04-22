package RBT;

import java.util.ArrayList;
import java.util.Random;

import 红黑树.Hq_P32.RBTNode;

/*
 * 1.根节点是黑色的.
 * 2.每一个叶子节点是黑色的，
 * 3.如果一个结点本身为红色的，那他的孩子为黑色
 * 4.对于一个黑色的节点的右孩子为黑色
 * 5.从任意一个节点出发,经过的黑色节点数目一样的
 * 6.保持黑色平衡的二叉树  O(logn)
 */

/*
 * 2-3树与红黑树等价性
 * 2-3树：满足二分搜索树的性质 但不是二叉树 节点可以存放一个元素，另一中节点可以存放多个元素
 * 		: 每一个结点有2个或者3个孩子
 * 		性质：2-3树是一个绝对平衡的数
 * 						42
 * 					/		\
 * 			17 33			50
 * 			/\	\			/\
 * 	6 12	  18	37	48		66 88
 */
public class Hq_P32<K extends Comparable<K>,V>{
	private static final boolean  RED=true;
	private static final boolean  BLACK=false;
	
	private class Node{
		public K key;
        public V value;
        public Node left, right;
        public boolean color;//red black
        

        public Node(K key, V value){
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            color=RED; //红色结点：它与其它的父节点进行融合。在等价的2-3树中进行融合
        }
	}
	
	private Node root;
	private int size;
	public Hq_P32() {
		root=null;
		size=0;
	}
	
	
	public int getSize() {
		return size;
	}
	public boolean isEmpty() {
		return size==0;
	}
	   // 返回以node为根节点的二分搜索树中，key所在的节点
    private Node getNode(Node node, K key){

        if(node == null)
            return null;

        if(key.equals(node.key))
            return node;
        else if(key.compareTo(node.key) < 0)
            return getNode(node.left, key);
        else // if(key.compareTo(node.key) > 0)
            return getNode(node.right, key);
    }

    public boolean contains(K key){
        return getNode(root, key) != null;
    }

    public V get(K key){

        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public void set(K key, V newValue){
        Node node = getNode(root, key);
        if(node == null)
            throw new IllegalArgumentException(key + " doesn't exist!");

        node.value = newValue;
    }

    // 返回以node为根的二分搜索树的最小值所在的节点
    private Node minimum(Node node){
        if(node.left == null)
            return node;
        return minimum(node.left);
    }

	
    private void preOrder(Node node) {
        if(node != null) {
            System.out.print(node.key+" ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    public void preOrder() {
        preOrder(root);
    }

    /*
     * 中序遍历"红黑树"
     */
    private void inOrder(Node node) {
        if(node != null) {
            inOrder(node.left);
            System.out.print(node.key+" ");
            inOrder(node.right);
        }
    }

    public void inOrder() {
        inOrder(root);
    }


    /*
     * 后序遍历"红黑树"
     */
    private void postOrder(Node node) {
        if(node != null)
        {
            postOrder(node.left);
            postOrder(node.right);
            System.out.print(node.key+" ");
        }
    }

    public void postOrder() {
        postOrder(root);
    }


   
	//1.RBT 左旋转
	/*
	 *    43
	 *       \ 47
	 */
	private Node leftRotate(Node node) {
		Node x=node.right;
		
		//左旋转
		node.right=x.left;
		x.left=node;
		
		//维持结点颜色
		x.color=node.color;
		node.color=RED;//表示node与他的父节点 x 形成一个三节点
		
		return x;
		
	}
	/*
	 * 		42
	 * 		/
	 * 		37
	 * ->12
	 */
	//3. 右旋转
	private Node rightRotate(Node node) {
		Node x=node.left;
		//右旋转
		node.left=x.right;
		x.right=node;
		//维持节点颜色
		x.color=node.color;
		node.color=RED;
		
		return x;
	}
	/*
	 * 2..颜色翻转：
	 *    			42
	 *    			/
	 *    			37
	 *66->
	 */
	private void flipColors(Node node) {//颜色翻转
		node.color=RED;
		node.left.color=BLACK;
		node.right.color=BLACK;
		
	}
	

	private boolean isRed(Node node) {
		if(node==null) {
			return BLACK;
		}
		return node.color;
	}
	
	//递归向RBT中插入元素
	private Node add(Node node,K key,V value) {
		if(node==null) {
			size++;
			return new Node(key, value);//插入结点默认为红色
		}
		//通过key来确认插入的位置
		if(key.compareTo(node.key)<0) {
			node.left=add(node.left, key, value);
		}else if(key.compareTo(node.key)>0) {
			node.right=add(node.right, key, value);
		}else {
			node.value=value;
		}
		/*
		 * 		42
		 *    		56
		 *    			66
		 *  	
		 */
		if(isRed(node.right) && !isRed(node.left)) {//左旋转
			node=leftRotate(node);
		}
		/*
		 *   42 
		 *     
		 * 22
		 */
		if(isRed(node.left)&& isRed(node.left.left)) {//右旋转
			node=rightRotate(node);
		}
		//颜色翻转
		if(isRed(node.left)&&isRed(node.right)) {
			flipColors(node);
		}
		
		return node;
		// 维护红黑树时机:添加完节点以后回溯向上维护
	}
	
	//
	//add 添加元素
	//BLT添加元素  保持根节点为黑色
	public void  add (K key,V value) {
		root=add(root ,key, value);
		root.color=BLACK;
		
	}

	
	public static void main(String[] args) {
		System.out.println("RBT test.....");
		ArrayList<Integer> testArrayList=new ArrayList<Integer>();
		
		int a[] = {10, 40, 30, 60, 90, 70, 20, 50, 80};
		
		for(int i=0;i<a.length;i++) {
			testArrayList.add(a[i]);
		}
		
		Hq_P32<Integer, Integer>rbtTest=new Hq_P32();
		for(int i=0;i<testArrayList.size();i++) {
				rbtTest.add(a[i], 1);
		}
		
		System.out.println("是否为空："+rbtTest.isEmpty());
		System.out.println("长度："+rbtTest.getSize());
		
		System.out.println(rbtTest.root.key);
		System.out.print("中序遍历：");
		rbtTest.inOrder();
		System.out.println();
		System.out.print("先序遍历：");
		rbtTest.preOrder();
		
	}

}
