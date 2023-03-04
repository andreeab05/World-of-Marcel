public interface Visitor<T extends Entity> {
    public void visit(T entity);
}
