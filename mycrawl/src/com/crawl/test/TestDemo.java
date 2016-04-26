package com.crawl.test;
public class TestDemo{
    class ListNode{
        int val;
        ListNode next = null;
        ListNode(int val){
            this.val = val;
        }
    }
    public static void main(String args []){
        TestDemo t = new TestDemo();
        ListNode ln = t.initListNode();
        ln = t.inverse(ln);
        t.printListNode(ln);
    }

    /**
     * 创建一个链表
     * @return
     */
    public ListNode initListNode(){
        ListNode ln1 = new ListNode(1);
        ListNode ln2 = new ListNode(2);
        ListNode ln3 = new ListNode(3);
        ListNode ln4 = new ListNode(4);
        ListNode ln5 = new ListNode(5);
        ln1.next = ln2;
        ln2.next = ln3;
        ln3.next = ln4;
        ln4.next = ln5;
        return ln1;
    }

    /**
     * 链表逆置(循环方式)
     * @param ln
     * @return
     */
    public ListNode inverse(ListNode ln){
        if(ln == null || ln.next == null){
            return ln;
        }
        ListNode pre = null;
        ListNode nex = null;
        while (ln != null){
            nex = ln.next;
            ln.next = pre;
            pre = ln;
            ln = nex;
        }
        return pre;
    }
    public ListNode recursionInverse(ListNode ln){
        if(ln == null || ln.next == null){
            return ln;
        }
        ListNode reHead = recursionInverse(ln.next);
        ln.next.next = ln;
        ln.next = null;
        return reHead;
    }
    public void printListNode(ListNode ln){
        while (ln != null){
            System.out.println(ln.val);
            ln = ln.next;
        }
    }
}