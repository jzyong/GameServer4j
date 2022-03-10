package org.mmo.engine.util.math;

/**
 * 向量接口
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public interface Vector<T extends Vector<T>> {

    /**
     * Adds the given vector to this vector
     * 
     * @param v
     *            The vector
     * @return This vector for chaining
     */
    T add(T v);

    /**
     * Scales this vector by a scalar
     * 
     * @param scalar
     *            The scalar
     * @return This vector for chaining
     */
    T scl(float scalar);

    /**
     * Scales this vector by another vector
     * 
     * @return This vector for chaining
     */
    T scl(T v);

    /**
     * Sets this vector from the given vector
     * 
     * @param v
     *            The vector
     * @return This vector for chaining
     */
    T set(T v);

    /**
     * Normalizes this vector. Does nothing if it is zero.
     * 
     * @return This vector for chaining
     */
    T nor();

    /** @return The euclidean length */
    float len();

    /**
     * This method is faster than {@link Vector#len()} because it avoids calculating
     * a square root. It is useful for comparisons, but not for getting exact
     * lengths, as the return value is the square of the actual length.
     * 
     * @return The squared euclidean length
     */
    float len2();

    /**
     * First scale a supplied vector, then add it to this vector.
     * 
     * @param v
     *            addition vector
     * @param scalar
     *            for scaling the addition vector
     */
    T mulAdd(T v, float scalar);

    /**
     * 坐标是否相等
     * 
     * @param precision
     *            精度
     * @return
     */
    default boolean equal(Vector3 vector3, float precision) {
        return false;
    }
    
    /** Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
	 * in this vector.
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining. */
	T lerp (T target, float alpha);

    /** @return Whether this vector is a zero vector */
    boolean isZero ();

    /** @return Whether the length of this vector is smaller than the given margin */
    boolean isZero (final float margin);

    /** Sets the components of this vector to 0
     * @return This vector for chaining */
    T setZero ();

    /** @return a copy of this vector */
    T cpy ();

    /** Subtracts the given vector from this vector.
     * @param v The vector
     * @return This vector for chaining */
    T sub (T v);

    /** Limits the length of this vector, based on the desired maximum length.
     * @param limit desired maximum length for this vector
     * @return this vector for chaining */
    T limit (float limit);

    /** Limits the length of this vector, based on the desired maximum length squared.
     * <p />
     * This method is slightly faster than limit().
     * @param limit2 squared desired maximum length for this vector
     * @return this vector for chaining
     * @see #len2() */
    T limit2 (float limit2);

    /** @param v The other vector
     * @return the distance between this and the other vector */
    float dst (T v);

    /** @param v The other vector
     * @return The dot product between this and the other vector */
    float dot (T v);

    /** This method is faster than {@link Vector#dst(Vector)} because it avoids calculating a square root. It is useful for
     * comparisons, but not for getting accurate distances, as the return value is the square of the actual distance.
     * @param v The other vector
     * @return the squared distance between this and the other vector */
    float dst2 (T v);

    /** Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
     * @param other
     * @param epsilon
     * @return whether the vectors have fuzzy equality. */
    boolean epsilonEquals (T other, float epsilon);
}
