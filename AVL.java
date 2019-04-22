package avl;

import java.util.ArrayList;

import org.omg.CORBA.portable.ValueBase;

/*
  * 平衡二叉树 ：对于任意一个节点，左子树和右子树的高度差不能超过11
  * 完全二叉树：最大深度值 - 最小深度值<=1
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
		public int height;// 记录当前结点的高度值

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

	// 获得高度值
	private int getHeight(Node node) {
		if (node == null) {
			return 0;
		}
		return node.height;
	}

	// 获得每个node的平衡因子
	// 只有每个结点的平衡因子<=1才会是平衡的
	private int getBalanceFactor(Node node) {
		if (node == null) {
			return 0;
		}
		return getHeight(node.left) - getHeight(node.right);
	}

	// 向node为跟的二分搜索树里面添加一个key value值，使用递归
	public void add(K keys, V vaule) {
		root = add(root, keys, vaule);
	}

	// 二分搜索树：它中序遍历是一个 升序遍历，检查是否是一个二分搜搜索数
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

	// 中序遍历
	private void inOrder(Node node, ArrayList<K> keys) {
		if (node == null) {
			return;
		}
		inOrder(node.left, keys);
		keys.add(node.key);
		inOrder(node.right, keys);
		
	}
	// 具体实现插入一个新的节点

	// 平衡树：任意结点的左右子树的高度差不超过1
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
		// 如果当前结点为平衡，则继续看当前结点的左右子树结点是否为平衡
		return isBalanced(node.left) && isBalanced(node.right);
	}

	// LL ：当前的 结点的平衡因子>1 && 当前结点的左子树结点的平衡因子>0 --->右旋转
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

		// 旋转之后要更新height值
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

		// 旋转之后要更新height值
		C.height = Math.max(getHeight(C.left), getHeight(C.right)) + 1;
		B.height = Math.max(getHeight(B.left), getHeight(B.right)) + 1;

		return B;
	}

	/*
	 * //返回以node为跟结点的二分搜索树中，key所在的节点 private Node getNode(Node node,K key) {
	 * if(node==null) { return null; } if(key.equals(node.key)) return node; return
	 * root;
	 * 
	 * }
	 */
	private Node add(Node node, K key, V value) {
		// 当前要添加的节点，一定在叶子结点上，所以它为空，的创建结点
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

		// 添加了一个新的结点之后欧，他们的height会改变
		node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

		// 且平衡因子也会改变，要进行更新
		int BalanceFactor = getBalanceFactor(node);

		// 如果Math.abs(BalanceFactor)>1 说明他是不是平衡
		/*
		 * if(Math.abs(BalanceFactor)>1) {
		 * 
		 * }
		 */

		// 添加了元素之后，要进行平衡维护
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
	 * rNode;//保存需要的node if(key.compareTo(node.key)<0) { node.left=del(node.right,
	 * key); }else if(key.compareTo(node.key)>0){ node.right=del(node.right, key); }
	 * else {//当前结点 key等于 要上删除的节点 //如果要删除的节点左子树为空 if(node.left==null) { Node
	 * rightNode=node.right; node.right=null; size--; rNode= rightNode; }
	 * //如果要删除的节点右子树为空 if(node.right==null) { Node leftNode=node.left;
	 * node.left=null; size--; rNode=leftNode; } //如果要删除的节点左右子树不为空
	 * 
	 * 
	 * Node successor=minimum();
	 * 
	 * successor.right=del(node.right, key); successor.left=node.left;
	 * 
	 * node.left=node.right=null; rNode=successor; }
	 * 
	 * // 删除之后 维护平衡
	 * 
	 * //添加了一个新的结点之后欧，他们的height会改变 rNode.height=Math.max(getHeight(rNode.left),
	 * getHeight(rNode.right))+1;
	 * 
	 * //且平衡因子也会改变，要进行更新 int BalanceFactor=getBalanceFactor(rNode);
	 * 
	 * //如果Math.abs(BalanceFactor)>1 说明他是不是平衡
	 * 
	 * if(Math.abs(BalanceFactor)>1) {
	 * 
	 * }
	 * 
	 * 
	 * //添加了元素之后，要进行平衡维护 if(BalanceFactor>1 && getBalanceFactor(rNode.left)>=0) {
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
