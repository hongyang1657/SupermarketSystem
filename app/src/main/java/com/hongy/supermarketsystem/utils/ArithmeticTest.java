package com.hongy.supermarketsystem.utils;

public class ArithmeticTest {

    /**
     * 验证回文字符串
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {
        String str = s.replaceAll("[^0-9a-zA-Z]", "").toLowerCase();
        for (int i=0;i<str.length();i++){
            if (str.charAt(i)!=str.charAt(str.length()-1-i)){
                return false;
            }
        }

        return true;

    }

    public void deleteNode(ListNode node) {
    }

    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
}
