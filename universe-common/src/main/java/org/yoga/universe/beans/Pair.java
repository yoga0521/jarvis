package org.yoga.universe.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @Description: pair value
 * @Author: yoga
 * @Date: 2022/5/11 10:11
 */
public class Pair<L, R> implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {

    private static final long serialVersionUID = 6483044373838966630L;

    /**
     * a pair of nulls
     */
    @SuppressWarnings("rawtypes")
    private static final Pair NULL = of(null, null);

    /**
     * left object
     */
    private L left;
    /**
     * right object
     */
    private R right;

    /**
     * create a new pair instance
     *
     * @param left  the left value, may be null
     * @param right the right value, may be null
     */
    private Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * this factory create pair
     *
     * @param left  the left element, may be null
     * @param right the right element, may be null
     * @param <L>   the left element type
     * @param <R>   the right element type
     * @return a pair from the two parameters
     */
    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }

    /**
     * create a pair of nulls
     *
     * @param <L> the left element type
     * @param <R> the right element type
     * @return a pair of nulls
     */
    @SuppressWarnings("unchecked")
    public static <L, R> Pair<L, R> nullPair() {
        return NULL;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    /**
     * get the key from this pair
     * <p>
     * this method implements the {@code Map.Entry} interface returning the left element as the key
     *
     * @return the left element as the key, may be null
     */
    @Override
    public L getKey() {
        return getLeft();
    }

    /**
     * get the value from this pair
     * <p>
     * this method implements the {@code Map.Entry} interface returning the right element as the value
     *
     * @return the right element as the value, may be null
     */
    @Override
    public R getValue() {
        return getRight();
    }

    /**
     * set the {@code Map.Entry} value.
     * <p>
     * this set the right element of the pair.
     *
     * @param value the right value to set
     * @return the old value for the right element
     */
    @Override
    public R setValue(R value) {
        final R result = getRight();
        setRight(value);
        return result;
    }

    /**
     * compare the pair based on the left element followed by the right element
     * the types must be {@code Comparable}
     *
     * @param other the other pair
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(Pair<L, R> other) {
        // todo
        return 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry<?, ?>) {
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            return Objects.equals(getKey(), other.getKey())
                    && Objects.equals(getValue(), other.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getKey() == null ? 0 : getKey().hashCode()) ^
                (getValue() == null ? 0 : getValue().hashCode());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Pair.class.getSimpleName() + "{", "}")
                .add("left=" + left)
                .add("right=" + right)
                .toString();
    }
}
