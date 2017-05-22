package minheap;

/**
 * 堆排序，采用顺序存储 大根堆
 * 
 * @author 超超boy
 */
public class HeapSort2 {

    int[] arr;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        HeapSort2 heapSor = new HeapSort2();
        int[] arr = { 7, 23, 45, 9, 40, 73, 12, 49 }; // 0下标放的是数组长度，
        heapSor.arr = arr;
        heapSor.heapSort(arr);

        for (int i = 0; i < arr.length; i++)
            System.out.print(".." + arr[i]);
    }

    void heapAdjust(int[] arr, int s, int m) {
        // 已知arr[s...m]中记录的关键字除arr[s]之外均满足堆的定义，本函数调整arr[s]的关键字，使arr[s...m]成为一个最大堆
        int rc = arr[s]; // s是最后一个非叶子节点

        for (int j = 2 * s; j <= m; j = s * 2) {
            if (j < m && arr[j] < arr[j + 1]) j++; // j为key较大的下标
            if (rc >= arr[j]) break;
            arr[s] = arr[j]; // 上移到父节点
            s = j;
        }
        arr[s] = rc; // 要放入的位置

    }

    void heapSort(int[] arr) {
        // 对数组进行建堆操作，就是从最后一个非叶结点进行筛选的过程
        for (int i = (arr.length - 1) / 2; i > 0; i--) {// 因为0没有使用，所以length-1
            heapAdjust(arr, i, arr.length - 1);
        }
        System.out.println("........建堆完成.............");

        outputArr(1);
        for (int i = arr.length - 1; i > 1; i--) {
            int temp = arr[1];
            arr[1] = arr[i];
            arr[i] = temp;
            heapAdjust(arr, 1, i - 1);
        }
    }

    void outputArr(int i) {

        if (i <= arr[0]) {
            System.out.println("___"+arr[i]);
            outputArr(i * 2); // 左
            outputArr(i * 2 + 1); // 右
        }
    }
}
