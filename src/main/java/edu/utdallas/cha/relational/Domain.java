package edu.utdallas.cha.relational;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class Domain<T> implements Iterable<Integer> {
    protected final Map<T, Integer> indexMap;
    protected final List<T> keys;
    protected int size;

    protected Domain() {
        this.size = 0;
        this.indexMap = new HashMap<>();
        this.keys = new ArrayList<>();
    }

    public int add(T key) {
        final Integer idx = this.indexMap.get(key);
        if(idx == null) {
            this.indexMap.put(key, this.size++);
            this.keys.add(key);
            return this.size - 1;
        }
        return idx;
    }

    public Integer indexOf(T key) {
        return this.indexMap.get(key);
    }

    public T keyOf(int index) {
        if(index >= size || size < 0)
            return null;
        return keys.get(index);
    }

    public int size() {
        return this.size;
    }

    public abstract String name();

    public void save() throws Exception {
        final File domFile = new File(name() + ".dom");
        domFile.deleteOnExit();
        try (final PrintWriter out = new PrintWriter(domFile)) {
            out.println(name() + " " + size());
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return IntStream.range(0, this.size).iterator();
    }
}