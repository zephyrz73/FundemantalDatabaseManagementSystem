package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    Map<Integer,TDItem> schema = new HashMap<>();
    int tupleSize = 0;

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldType + "(" + fieldName + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return this.schema.values().iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        boolean fieldArNull = fieldAr == null;
        for (int i = 0; i < typeAr.length; i++) {
            this.tupleSize += (typeAr[i] == Type.INT_TYPE) ? 4 : Type.STRING_LEN;
            String name = (fieldArNull || fieldAr[i] == null) ? null : fieldAr[i];
            TDItem item = new TDItem(typeAr[i], name);
            this.schema.put(i,item);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        for (int i = 0; i < typeAr.length; i++) {
            this.tupleSize += (typeAr[i] == Type.INT_TYPE) ? 4 : Type.STRING_LEN;
            TDItem item = new TDItem(typeAr[i], null);
            this.schema.put(i, item);
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return schema.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        // i start from 0
        if (i >= this.schema.size() || i < 0) {
            throw new NoSuchElementException();
        } else {
            return schema.get(i).fieldName;
        }
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
        // i start from 0
        if (i >= this.schema.size() || i < 0) {
            throw new NoSuchElementException();
        } else {
            return schema.get(i).fieldType;
        }
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
        for (int i = 0; i < this.schema.size(); i++) {
            String fieldName = this.schema.get(i).fieldName;
            if (fieldName == null && name == null) {
                return i;
            }
            if (this.schema.get(i).fieldName != null && name != null) {
                if (this.schema.get(i).fieldName.equals(name)) {
                    return i;
                }
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        return this.tupleSize;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        if (td2.numFields() == 0) {
            return td1;
        }
        if (td1.numFields() == 0) {
            return td2;
        }
        // some code goes here
        int mergeLength = td1.numFields() + td2.numFields();
        Type[] mergeType = new Type[mergeLength];
        String[] mergeName = new String[mergeLength];
        for (int i = 0; i < mergeLength; i++) {
            boolean ifFirst = i < td1.numFields();
            mergeType[i] = (ifFirst) ? td1.schema.get(i).fieldType : td2.schema.get(i - td1.numFields()).fieldType;
            mergeName[i] = (ifFirst) ? td1.schema.get(i).fieldName : td2.schema.get(i - td1.numFields()).fieldName;
        }
        return new TupleDesc(mergeType, mergeName);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */

    public boolean equals(Object o) {
        // some code goes here
        if (! (o instanceof TupleDesc)) {
            return false;
        } else if (this.numFields() != ((TupleDesc) o).numFields()) {
            return false;
        } else {
            for (int i = 0; i < this.numFields(); i++) {
                if (this.schema.get(i).fieldType.getLen()
                        != ((TupleDesc) o).schema.get(i).fieldType.getLen()) {
                    return false;
                }
            }
            return true;
        }
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        StringBuilder stringSchema = new StringBuilder();
        for (int i = 0; i < schema.size(); i++) {
            stringSchema.append(schema.get(i).fieldType);
            stringSchema.append("(").append(schema.get(i).fieldName).append("),");
        }
        return stringSchema.toString();
    }
}
