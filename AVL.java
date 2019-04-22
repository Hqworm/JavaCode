package avl;

import java.util.ArrayList;

import org.omg.CORBA.portable.ValueBase;

/*
  * ƽ������� ����������һ���ڵ㣬���������������ĸ߶Ȳ�ܳ���11
  * ��ȫ��������������ֵ - ��С���ֵ<=1
 * 	
 */

public class Hq_P31<K extends Comparable<K>, V> {
	public static void main(String[] args) {
		Hq_P31 bst = new Hq_P31();
		ArrayList<Integer> arry=new ArrayList<>();
		for (int i=0;i<10;i++) {
			arry.add(i);
		}
		bst.inOrder(bst.root, arry);
		System.out.println(bst.isBalanced());
	}
	private class Node {
		private K key;
		private V value;
		public Node left;
		public Node right;
		public int height;// ��¼��ǰ���ĸ߶�ֵ

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
			left = null;
			right = null;
			height = 1;

		}
	}

	private Node root;
	private int size;

	public Hq_P31() {
		root = null;
		size = 0;
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	// ��ø߶�ֵ
	private int getHeight(Node node) {
		if (node == null) {
			return 0;
		}
		return node.height;
	}

	// ���ÿ��node��ƽ������
	// ֻ��ÿ������ƽ������<=1�Ż���ƽ���
	private int getBalanceFactor(Node node) {
		if (node == null) {
			return 0;
		}
		return getHeight(node.left) - getHeight(node.right);
	}

	// ��nodeΪ���Ķ����������������һ��key valueֵ��ʹ�õݹ�
	public void add(K keys, V vaule) {
		root = add(root, keys, vaule);
	}

	// �����������������������һ�� �������������Ƿ���һ��������������
	public boolean  inOrder(Node node) {
		ArrayList<K> keys = new ArrayList<K>();
		inOrder(root, keys);
		for (int i = 0; i < keys.size(); i++) {
			if (keys.get(i - 1).compareTo(keys.get(i)) > 0) {
				return false;
			}
		}
		return true;
	}

	// �������
	private void inOrder(Node node, ArrayList<K> keys) {
		if (node == null) {
			return;
		}
		inOrder(node.left, keys);
		keys.add(node.key);
		inOrder(node.right, keys);
		
	}
	// ����ʵ�ֲ���һ���µĽڵ�

	// ƽ����������������������ĸ߶Ȳ����1
	public boolean isBalanced() {
		return isBalanced(root);
	}

	public boolean isBalanced(Node node) {
		if (node == null) {
			return true;
		}

		int BalanceFactor = getBalanceFactor(node);

		if (Math.abs(BalanceFactor) > 1) {
			return false;
		}
		// �����ǰ���Ϊƽ�⣬���������ǰ����������������Ƿ�Ϊƽ��
		return isBalanced(node.left) && isBalanced(node.right);
	}

	// LL ����ǰ�� ����ƽ������>1 && ��ǰ��������������ƽ������>0 --->����ת
	/*
	 * C
	 * 
	 * B T4
	 * 
	 * A T3
	 * 
	 * T1 T2
	 */
	private Node rightRotate(Node C) {
		Node B = C.left;
		Node T3 = B.right;
		B.right = C;
		C.left = T3;

		// ��ת֮��Ҫ����heightֵ
		C.height = Math.max(getHeight(C.left), getHeight(C.right));
		B.height = Math.max(getHeight(B.left), getHeight(B.right));

		return B;
	}

	/*
	 * C
	 * 
	 * T4 B
	 * 
	 * T3 a T2 T1
	 */
	private Node leftRotate(Node C) {
		Node B = C.right;
		Node T3 = B.left;
		B.left = C;
		C.right = T3;

		// ��ת֮��Ҫ����heightֵ
		C.height = Math.max(getHeight(C.left), getHeight(C.right)) + 1;
		B.height = Math.max(getHeight(B.left), getHeight(B.right)) + 1;

		return B;
	}

	/*
	 * //������nodeΪ�����Ķ����������У�key���ڵĽڵ� private Node getNode(Node node,K key) {
	 * if(node==null) { return null; } if(key.equals(node.key)) return node; return
	 * root;
	 * 
	 * }
	 */
	private Node add(Node node, K key, V value) {
		// ��ǰҪ��ӵĽڵ㣬һ����Ҷ�ӽ���ϣ�������Ϊ�գ��Ĵ������
		if (node == null) {
			size++;
			return new Node(key, value);
		}

		if (key.compareTo(node.key) < 0) {
			node.left = add(node.left, key, value);
		} else if (key.compareTo(node.key) > 0) {
			node.right = add(node.right, key, value);
		} else {
			node.value = value;
		}

		// �����һ���µĽ��֮��ŷ�����ǵ�height��ı�
		node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

		// ��ƽ������Ҳ��ı䣬Ҫ���и���
		int BalanceFactor = getBalanceFactor(node);

		// ���Math.abs(BalanceFactor)>1 ˵�����ǲ���ƽ��
		/*
		 * if(Math.abs(BalanceFactor)>1) {
		 * 
		 * }
		 */

		// �����Ԫ��֮��Ҫ����ƽ��ά��
		if (BalanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
			return rightRotate(node);
		}
		if (BalanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
			return leftRotate(node);
		}
		/*
		 * C B A
		 */
		// LR
		if (BalanceFactor > 1 && getBalanceFactor(node.left) < 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}
		// RL
		if (BalanceFactor < -1 && getBalanceFactor(node.right) > 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		return node;
	}


	/*
	 * public void add(K key, V value) { add(root,key,value); }
	 */
	 
	/*
	 * private Node del(Node node,K key) { if(node==null) { return null; } Node
	 * rNode;//������Ҫ��node if(key.compareTo(node.key)<0) { node.left=del(node.right,
	 * key); }else if(key.compareTo(node.key)>0){ node.right=del(node.right, key); }
	 * else {//��ǰ��� key���� Ҫ��ɾ���Ľڵ� //���Ҫɾ���Ľڵ�������Ϊ�� if(node.left==null) { Node
	 * rightNode=node.right; node.right=null; size--; rNode= rightNode; }
	 * //���Ҫɾ���Ľڵ�������Ϊ�� if(node.right==null) { Node leftNode=node.left;
	 * node.left=null; size--; rNode=leftNode; } //���Ҫɾ���Ľڵ�����������Ϊ��
	 * 
	 * 
	 * Node successor=minimum();
	 * 
	 * successor.right=del(node.right, key); successor.left=node.left;
	 * 
	 * node.left=node.right=null; rNode=successor; }
	 * 
	 * // ɾ��֮�� ά��ƽ��
	 * 
	 * //�����һ���µĽ��֮��ŷ�����ǵ�height��ı� rNode.height=Math.max(getHeight(rNode.left),
	 * getHeight(rNode.right))+1;
	 * 
	 * //��ƽ������Ҳ��ı䣬Ҫ���и��� int BalanceFactor=getBalanceFactor(rNode);
	 * 
	 * //���Math.abs(BalanceFactor)>1 ˵�����ǲ���ƽ��
	 * 
	 * if(Math.abs(BalanceFactor)>1) {
	 * 
	 * }
	 * 
	 * 
	 * //�����Ԫ��֮��Ҫ����ƽ��ά�� if(BalanceFactor>1 && getBalanceFactor(rNode.left)>=0) {
	 * return rightRotate(rNode); } if(BalanceFactor<-1 &&
	 * getBalanceFactor(rNode.right)<=0) { return leftRotate(rNode); }
	 * 
	 * C B A
	 * 
	 * //LR if(BalanceFactor>1 && getBalanceFactor(rNode.left)<0) {
	 * rNode.left=leftRotate(rNode.left); return rightRotate(rNode); } // RL
	 * if(BalanceFactor<-1 && getBalanceFactor(rNode.right)>0) {
	 * rNode.right=rightRotate(rNode.right); return leftRotate(rNode); }
	 * 
	 * }
	 */

}
