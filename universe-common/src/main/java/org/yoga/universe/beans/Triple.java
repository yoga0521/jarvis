/*
 * Copyright 2022 yoga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yoga.universe.beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @Description: triple value
 * @Author: yoga
 * @Date: 2022/5/11 10:22
 */
public class Triple<L, M, R> implements Comparable<Triple<L, M, R>>, Serializable {

    private static final long serialVersionUID = -9018036835751712194L;

    /**
     * a triple of nulls
     */
    @SuppressWarnings("rawtypes")
    private static final Triple NULL = of(null, null, null);

    /**
     * left object
     */
    public L left;
    /**
     * middle object
     */
    public M middle;
    /**
     * right object
     */
    public R right;

    /**
     * create a new triple instance.
     *
     * @param left   the left value, may be null
     * @param middle the middle value, may be null
     * @param right  the right value, may be null
     */
    private Triple(final L left, final M middle, final R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    /**
     * this factory create triple
     *
     * @param left   the left element, may be null
     * @param middle the middle element, may be null
     * @param right  the right element, may be null
     * @param <L>    the left element type
     * @param <M>    the middle element type
     * @param <R>    the right element type
     * @return a triple from the three parameters, not null
     */
    public static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
        return new Triple<>(left, middle, right);
    }

    /**
     * create a triple of nulls
     *
     * @param <L> the left element type
     * @param <M> the middle element type
     * @param <R> the right element type
     * @return a triple of nulls
     */
    @SuppressWarnings("unchecked")
    public static <L, M, R> Triple<L, M, R> nullTriple() {
        return NULL;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public M getMiddle() {
        return middle;
    }

    public void setMiddle(M middle) {
        this.middle = middle;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    /**
     * compare the triple based on the left element followed by the right element
     * the types must be {@code Comparable}
     *
     * @param other the other triple, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final Triple<L, M, R> other) {
        // todo
        return 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Triple<?, ?, ?>) {
            final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
            return Objects.equals(getLeft(), other.getLeft())
                    && Objects.equals(getMiddle(), other.getMiddle())
                    && Objects.equals(getRight(), other.getRight());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getLeft() == null ? 0 : getLeft().hashCode()) ^
                (getMiddle() == null ? 0 : getMiddle().hashCode()) ^
                (getRight() == null ? 0 : getRight().hashCode());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Triple.class.getSimpleName() + "{", "}")
                .add("left=" + left)
                .add("middle=" + middle)
                .add("right=" + right)
                .toString();
    }
}
