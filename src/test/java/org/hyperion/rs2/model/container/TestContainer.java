package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class TestContainer implements ContainerListener {

    public static final int CAP = 28;

    private Container container;
    private boolean itemChangedFired = false;
    private boolean itemsChangedFired = false;
    private int slot = -1;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        ItemDefinition.init();
    }

    @BeforeEach
    public void setUp() throws Exception {
        container = new Container(Container.Type.STANDARD, CAP);
        itemChangedFired = false;
        itemsChangedFired = false;
        slot = -1;
    }

    @Test
    public void testGetListeners() {
        final ContainerListener listener1 = new ContainerListener() {
            @Override
            public void itemChanged(final Container container, final int slot) {
            }

            @Override
            public void itemsChanged(final Container container, final int[] slots) {
            }

            @Override
            public void itemsChanged(final Container container) {
            }
        };
        final ContainerListener listener2 = new ContainerListener() {
            @Override
            public void itemChanged(final Container container, final int slot) {
            }

            @Override
            public void itemsChanged(final Container container, final int[] slots) {
            }

            @Override
            public void itemsChanged(final Container container) {
            }
        };
        container.addListener(listener1);
        container.addListener(listener2);
        final Collection<ContainerListener> listeners = container.getListeners();
        assertEquals(2, listeners.size());
        assertTrue(listeners.contains(listener1));
        assertTrue(listeners.contains(listener2));
    }

    @Test
    public void testAddListener() {
        assertEquals(0, container.getListeners().size());
        final ContainerListener listener = new ContainerListener() {
            @Override
            public void itemChanged(final Container container, final int slot) {
            }

            @Override
            public void itemsChanged(final Container container, final int[] slots) {
            }

            @Override
            public void itemsChanged(final Container container) {
            }
        };
        container.addListener(listener);
        assertEquals(1, container.getListeners().size());
    }

    @Test
    public void testRemoveListener() {
        final ContainerListener listener = new ContainerListener() {
            @Override
            public void itemChanged(final Container container, final int slot) {
            }

            @Override
            public void itemsChanged(final Container container, final int[] slots) {
            }

            @Override
            public void itemsChanged(final Container container) {
            }
        };
        container.addListener(listener);
        container.removeListener(listener);
        assertEquals(0, container.getListeners().size());
    }

    @Test
    public void testRemoveAllListeners() {
        final ContainerListener listener1 = new ContainerListener() {
            @Override
            public void itemChanged(final Container container, final int slot) {
            }

            @Override
            public void itemsChanged(final Container container, final int[] slots) {
            }

            @Override
            public void itemsChanged(final Container container) {
            }
        };
        final ContainerListener listener2 = new ContainerListener() {
            @Override
            public void itemChanged(final Container container, final int slot) {
            }

            @Override
            public void itemsChanged(final Container container, final int[] slots) {
            }

            @Override
            public void itemsChanged(final Container container) {
            }
        };
        container.addListener(listener1);
        container.addListener(listener2);
        container.removeAllListeners();
        assertEquals(0, container.getListeners().size());
    }

    @Test
    public void testShift() {
        container.addListener(this);
        final Item item1 = new Item(995, 999999999);
        final Item item2 = new Item(995, 999999999);
        final Item item3 = new Item(995, 999999999);
        container.set(0, item1);
        container.set(5, item2);
        container.set(10, item3);
        container.shift();
        assertTrue(itemsChangedFired);
        assertEquals(item1, container.get(0));
        assertEquals(item2, container.get(1));
        assertEquals(item3, container.get(2));
        assertNull(container.get(5));
        assertNull(container.get(10));
        assertEquals(3, container.size());
    }

    @Test
    public void testFreeSlot() {
        assertEquals(0, container.freeSlot());
        container.set(0, new Item(995));
        assertEquals(1, container.freeSlot());
        container.set(1, new Item(995));
        assertEquals(2, container.freeSlot());
        container.set(0, null);
        assertEquals(0, container.freeSlot());
    }

    @Test
    public void testAdd() {
        container.addListener(this);
        assertTrue(container.add(new Item(995)));
        assertTrue(itemChangedFired);
        assertEquals(0, slot);
        this.itemChangedFired = false;
        this.slot = -1;
    }

    @Test
    public void testGet() {
        final Item item = new Item(995);
        container.set(0, item);
        assertEquals(item, container.get(0));
    }

    @Test
    public void testSet() {
        final Item item = new Item(995);
        container.set(0, item);
        assertEquals(item, container.get(0));
    }

    @Test
    public void testCapacity() {
        assertEquals(CAP, container.capacity());
    }

    @Test
    public void testSize() {
        assertEquals(0, container.size());
        container.set(0, new Item(995));
        assertEquals(1, container.size());
        container.set(1, new Item(995));
        assertEquals(2, container.size());
        container.clear();
        assertEquals(0, container.size());
    }

    @Test
    public void testClear() {
        container.addListener(this);
        container.set(0, new Item(995));
        container.set(1, new Item(995));
        container.clear();
        assertEquals(0, container.size());
        assertTrue(itemsChangedFired);
    }

    @Override
    public void itemChanged(final Container container, final int slot) {
        assertEquals(this.container, container);
        this.itemChangedFired = true;
        this.slot = slot;
    }

    @Override
    public void itemsChanged(final Container container, final int[] slots) {
        assertEquals(this.container, container);
        this.itemsChangedFired = true;
    }

    @Override
    public void itemsChanged(final Container container) {
        assertEquals(this.container, container);
        this.itemsChangedFired = true;
    }

}
