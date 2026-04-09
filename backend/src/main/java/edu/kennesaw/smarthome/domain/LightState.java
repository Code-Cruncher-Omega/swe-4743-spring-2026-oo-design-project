interface LightState {
    private final String name;
    public enum Transitions {OFF, ON};
    private List<Transitions> availableTransitions;
    public TransitionResult Execute()
}