package RBT;

import java.util.ArrayList;
import java.util.Random;

import �����.Hq_P32.RBTNode;

/*
 * 1.���ڵ��Ǻ�ɫ��.
 * 2.ÿһ��Ҷ�ӽڵ��Ǻ�ɫ�ģ�
 * 3.���һ����㱾��Ϊ��ɫ�ģ������ĺ���Ϊ��ɫ
 * 4.����һ����ɫ�Ľڵ���Һ���Ϊ��ɫ
 * 5.������һ���ڵ����,�����ĺ�ɫ�ڵ���Ŀһ����
 * 6.���ֺ�ɫƽ��Ķ�����  O(logn)
 */

/*
 * 2-3���������ȼ���
 * 2-3����������������������� �����Ƕ����� �ڵ���Դ��һ��Ԫ�أ���һ�нڵ���Դ�Ŷ��Ԫ��
 * 		: ÿһ�������2������3������
 * 		���ʣ�2-3����һ������ƽ�����
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
            color=RED; //��ɫ��㣺���������ĸ��ڵ�����ںϡ��ڵȼ۵�2-3���н����ں�
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
	   // ������nodeΪ���ڵ�Ķ����������У�key���ڵĽڵ�
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

    // ������nodeΪ���Ķ�������������Сֵ���ڵĽڵ�
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
     * �������"�����"
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
     * �������"�����"
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


   
	//1.RBT ����ת
	/*
	 *    43
	 *       \ 47
	 */
	private Node leftRotate(Node node) {
		Node x=node.right;
		
		//����ת
		node.right=x.left;
		x.left=node;
		
		//ά�ֽ����ɫ
		x.color=node.color;
		node.color=RED;//��ʾnode�����ĸ��ڵ� x �γ�һ�����ڵ�
		
		return x;
		
	}
	/*
	 * 		42
	 * 		/
	 * 		37
	 * ->12
	 */
	//3. ����ת
	private Node rightRotate(Node node) {
		Node x=node.left;
		//����ת
		node.left=x.right;
		x.right=node;
		//ά�ֽڵ���ɫ
		x.color=node.color;
		node.color=RED;
		
		return x;
	}
	/*
	 * 2..��ɫ��ת��
	 *    			42
	 *    			/
	 *    			37
	 *66->
	 */
	private void flipColors(Node node) {//��ɫ��ת
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
	
	//�ݹ���RBT�в���Ԫ��
	private Node add(Node node,K key,V value) {
		if(node==null) {
			size++;
			return new Node(key, value);//������Ĭ��Ϊ��ɫ
		}
		//ͨ��key��ȷ�ϲ����λ��
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
		if(isRed(node.right) && !isRed(node.left)) {//����ת
			node=leftRotate(node);
		}
		/*
		 *   42 
		 *     
		 * 22
		 */
		if(isRed(node.left)&& isRed(node.left.left)) {//����ת
			node=rightRotate(node);
		}
		//��ɫ��ת
		if(isRed(node.left)&&isRed(node.right)) {
			flipColors(node);
		}
		
		return node;
		// ά�������ʱ��:�����ڵ��Ժ��������ά��
	}
	
	//
	//add ���Ԫ��
	//BLT���Ԫ��  ���ָ��ڵ�Ϊ��ɫ
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
		
		System.out.println("�Ƿ�Ϊ�գ�"+rbtTest.isEmpty());
		System.out.println("���ȣ�"+rbtTest.getSize());
		
		System.out.println(rbtTest.root.key);
		System.out.print("���������");
		rbtTest.inOrder();
		System.out.println();
		System.out.print("���������");
		rbtTest.preOrder();
		
	}

}
