package com.example.contacts;

import java.util.Comparator;
import java.util.LinkedList;

public class Sorter {
    public void quickSort(LinkedList<Contact> list, Comparator<Contact> comparator) {
        if (list == null || list.size() == 0) {
            return;
        }
        quickSortRecursive(list, 0, list.size() - 1, comparator);
    }

    private void quickSortRecursive(LinkedList<Contact> list, int start, int end, Comparator<Contact> comparator) {
        if (start >= end) {
            return;
        }
        int pivotIndex = partition(list, start, end, comparator);
        quickSortRecursive(list, start, pivotIndex - 1, comparator);
        quickSortRecursive(list, pivotIndex + 1, end, comparator);
    }

    private int partition(LinkedList<Contact> list, int start, int end, Comparator<Contact> comparator) {
        Contact pivot = list.get(end);
        int pivotIndex = start;
        for (int i = start; i < end; i++) {
            if (comparator.compare(list.get(i), pivot) < 0) {
                swap(list, i, pivotIndex);
                pivotIndex++;
            }
        }
        swap(list, pivotIndex, end);
        return pivotIndex;
    }

    private void swap(LinkedList<Contact> list, int i, int j) {
        Contact temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    public static void insertionSortPhoneNumbers(LinkedList<Contact> contacts) {
        for (int i = 1; i < contacts.size(); i++) {
            Contact current = contacts.get(i);
            int j = i - 1;
            while (j >= 0 && contacts.get(j).getPhone().compareTo(current.getPhone()) > 0) {
                contacts.set(j + 1, contacts.get(j));
                j--;
            }
            contacts.set(j + 1, current);
        }
    }

    public void sortByEmailUsingShellSort(LinkedList<Contact> contacts) {
        int gap = contacts.size() / 2;
        while (gap > 0) {
            for (int i = gap; i < contacts.size(); i++) {
                Contact temp = contacts.get(i);
                int j;
                for (j = i; j >= gap && contacts.get(j - gap).getEmail().compareToIgnoreCase(temp.getEmail()) > 0; j -= gap) {
                    contacts.set(j, contacts.get(j - gap));
                }
                contacts.set(j, temp);
            }
            gap /= 2;
        }
    }


    public void sortByRecentContact(LinkedList<Contact> contacts){
        contacts.sort((c1, c2) -> c2.getDateAdded().compareTo(c1.getDateAdded()));
    }
    public void printSorted(LinkedList<Contact> contacts)  {
        System.out.println("Before sorting");
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            System.out.println(i + ". " + contact);
        }
        System.out.println();
        System.out.println("Sorted list");
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            System.out.println(i + ". " + contact);
        }
    }


}
