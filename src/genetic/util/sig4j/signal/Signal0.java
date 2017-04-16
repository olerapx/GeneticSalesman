package genetic.util.sig4j.signal;

import genetic.util.sig4j.ConnectionType;
import genetic.util.sig4j.Signal;
import genetic.util.sig4j.SlotDispatcher;
import genetic.util.sig4j.slot.Slot0;
import genetic.util.sig4j.Slot;

/**
 * A signal with 0 arguments.
 */
public class Signal0 extends Signal {

    /**
     * @see Signal#connect(Slot)
     */
    public void connect(final Slot0 slot) {
        super.connect(slot);
    }

    /**
     * @see Signal#connect(Slot, ConnectionType)
     */
    public void connect(final Slot0 slot, final ConnectionType type) {
        super.connect(slot, type);
    }

    /**
     * @see Signal#connect(SlotDispatcher, Slot)
     */
    public void connect(final SlotDispatcher dispatcher, final Slot0 slot) {
        super.connect(dispatcher, slot);
    }

    /**
     * @see Signal#emit(Object...)
     */
    public void emit() {
        super.emit();
    }

    @Override
    protected void actuate(final Slot slot, final Object[] args) {
        ((Slot0) slot).accept();
    }
}
