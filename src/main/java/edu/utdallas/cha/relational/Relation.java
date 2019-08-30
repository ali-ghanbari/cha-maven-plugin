package edu.utdallas.cha.relational;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public abstract class Relation<T> {
    protected final Set<T> data;

    protected Relation() {
        this.data = new HashSet<>();
    }

    public abstract String name();

    public void save() throws Exception {
        final File tuplesFile = new File(name() + ".tuples");
        tuplesFile.deleteOnExit();
        try (final PrintWriter pw = new PrintWriter(tuplesFile)) {
            pw.println("##");
            for (final T t : this.data) {
                pw.println(t);
            }
        }
    }

    protected abstract T convert(String s);

    public void load() throws Exception {
        final File tuplesFile = new File(name() + ".tuples");
        try (final BufferedReader br = new BufferedReader(new FileReader(tuplesFile))) {
            String line;
            line = br.readLine(); // ignore the header
            if (line == null) {
                return;
            }
            while ((line = br.readLine()) != null) {
                line = line.trim();
                this.data.add(convert(line));
            }
        }
    }
}
