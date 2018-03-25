package uk.gov.ida.helpers;

public final class Pair<T, U> {
  private final T left;
  private final U right;

  public Pair(T left, U right) {
    this.left = left;
    this.right = right;
  }

  public T getLeft() {
    return left;
  }

  public U getRight() {
    return right;
  }
}
