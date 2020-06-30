package example.atomic;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by shy on 20/6/30.
 */
public class LockFreeVector<E> {
    private static final int N_BUCKET = 30;
    private static final int FIRST_BUCKET_SIZE = 8;
    private static final int zeroNumFirst = 28;
    private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;
    private AtomicReference<Descriptor<E>> descRef;
    private boolean debug = false;

    public LockFreeVector() {
        this.buckets = new AtomicReferenceArray<AtomicReferenceArray<E>>(N_BUCKET);
        buckets.set(0, new AtomicReferenceArray<E>(FIRST_BUCKET_SIZE));
        descRef = new AtomicReference<>(new Descriptor<E>(0, null));
    }

    static class Descriptor<E> {
        public int size;
        volatile WriteDescriptor<E> writeop;

        public Descriptor(int size, WriteDescriptor<E> writeop) {
            this.size = size;
            this.writeop = writeop;
        }

        public void completeWrite() {
            WriteDescriptor<E> tmpOp = writeop;
            if (tmpOp != null) {
                tmpOp.doIt();
                writeop = null;
            }
        }
    }

    static class WriteDescriptor<E> {
        public E oldV;
        public E newV;
        public AtomicReferenceArray<E> addr;
        public int addr_ind;

        public WriteDescriptor(AtomicReferenceArray<E> addr, int addr_ind, E oldV, E newV) {
            this.addr = addr;
            this.addr_ind = addr_ind;
            this.oldV = oldV;
            this.newV = newV;
        }

        public void doIt() {
            addr.compareAndSet(addr_ind, oldV, newV);
        }
    }

    public void put(E e) {
        Descriptor<E> desc;
        Descriptor<E> newd;
        do {
            desc = descRef.get();
            desc.completeWrite();

            int pos = desc.size + FIRST_BUCKET_SIZE;
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            //计算buckets的下标
            int bucketInd = zeroNumFirst - zeroNumPos;
            if (buckets.get(bucketInd) == null) {
                int newLen = 2 * buckets.get(bucketInd - 1).length();
                if (debug) {
                    System.out.println("New Length is:" + newLen);
                }
                buckets.compareAndSet(bucketInd, null, new AtomicReferenceArray<E>(newLen));
            }

            //计算元素在具体bucket内的下标
            int idx = (0x80000000 >>> zeroNumPos) ^ pos;
            newd = new Descriptor<E>(desc.size + 1, new WriteDescriptor<E>(buckets.get(bucketInd), idx, null, e));
        } while (!descRef.compareAndSet(desc, newd));
        descRef.get().completeWrite();
    }

    public E get(int index) {
        int pos = index + FIRST_BUCKET_SIZE;
        int zeroNumPos = Integer.numberOfLeadingZeros(pos);
        //计算buckets的下标
        int bucketInd = zeroNumFirst - zeroNumPos;
        int idx = (0x80000000 >>> zeroNumPos) ^ pos;
        return buckets.get(bucketInd).get(idx);
    }
}
