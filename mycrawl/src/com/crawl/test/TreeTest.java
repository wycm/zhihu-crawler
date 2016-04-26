package com.crawl.test;

/**
 * Created by Administrator on 2016/4/18 0018.
 */
public class TreeTest {
    static class BinTree{
        int val;
        BinTree leftChild;
        BinTree rightChild;
        BinTree(int val){
            this.val = val;
            leftChild = null;
            rightChild = null;
        }
    }
    static class ValueGroup{
        int max;
        int min;
        ValueGroup(int max,int min){
            this.max = max;
            this.min = min;
        }
    }
    public static void main(String args []){
        TreeTest t = new TreeTest();
        BinTree root = t.init();
        ValueGroup vg = new ValueGroup(root.val,root.val);
        vg = t.preOrder(root,vg);
        System.out.println("value:" + (vg.max - vg.min));
    }
    public BinTree init(){
        BinTree A = new BinTree(1);
        BinTree B = new BinTree(2);
        BinTree C = new BinTree(3);
        BinTree D = new BinTree(4);
        BinTree E = new BinTree(5);
        A.leftChild = B;
        A.rightChild = E;
        B.leftChild = C;
        B.rightChild = D;
        return A;
    }
    /**
     * 先序遍历,根左右
     * @param root
     */
    public ValueGroup preOrder(BinTree root,ValueGroup vg){
        if(root != null){

            if(vg.max < root.val){
                vg.max = root.val;
            }
            if (vg.min > root.val){
                vg.min = root.val;
            }
            System.out.println("max:" + vg.max + "--min:" + vg.min);
            preOrder(root.leftChild,vg);
            preOrder(root.rightChild,vg);
        }else{
            return vg;
        }
        return vg;
    }
    /**
     * 中须遍历,左跟右
     */
    public void midOrder(BinTree root){
        if (root != null){
            midOrder(root.leftChild);
            System.out.println(root.val);
            midOrder(root.rightChild);
        }
    }
}
