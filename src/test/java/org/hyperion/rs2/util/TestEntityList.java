package org.hyperion.rs2.util;

import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.region.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEntityList {

    private EntityList<EntityStub> list;

    @BeforeEach
    public void setUp() throws Exception {
        list = new EntityList<>(10);
    }

    @Test
    public void testAdd() {
        list.add(new EntityStub());
        assertEquals(1, list.size());
    }

    @SuppressWarnings("serial")
    @Test
    public void testAddAll() {
        final List<EntityStub> stubs = new ArrayList<>() {{
            add(new EntityStub());
            add(new EntityStub());
            add(new EntityStub());
        }};
        list.addAll(stubs);
        assertEquals(3, list.size());
    }

    @Test
    public void testClear() {
        list.add(new EntityStub());
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    public void testContains() {
        final EntityStub stub = new EntityStub();
        assertFalse(list.contains(stub));
        list.add(stub);
        assertTrue(list.contains(stub));
        list.remove(stub);
        assertFalse(list.contains(stub));
    }

    @SuppressWarnings("serial")
    @Test
    public void testContainsAll() {
        final List<EntityStub> stubs = new ArrayList<>() {{
            add(new EntityStub());
            add(new EntityStub());
            add(new EntityStub());
        }};
        list.addAll(stubs);
        assertTrue(list.containsAll(stubs));
    }

    @Test
    public void testGet() {
        final EntityStub stub1 = new EntityStub();
        final EntityStub stub2 = new EntityStub();
        list.add(stub1);
        list.add(stub2);
        assertEquals(stub1, list.get(1));
        assertEquals(stub2, list.get(2));
    }

    @Test
    public void testIndexOf() {
        final EntityStub stub1 = new EntityStub();
        final EntityStub stub2 = new EntityStub();
        list.add(stub1);
        list.add(stub2);
        assertEquals(1, list.indexOf(stub1));
        assertEquals(2, list.indexOf(stub2));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(list.isEmpty());
        list.add(new EntityStub());
        assertFalse(list.isEmpty());
    }

    @Test
    public void testIterator() {
        final EntityStub stub1 = new EntityStub();
        final EntityStub stub2 = new EntityStub();
        list.add(stub1);
        list.add(stub2);
        final Iterator<EntityStub> it$ = list.iterator();
        assertTrue(it$.hasNext());
        assertEquals(stub1, it$.next());
        assertTrue(it$.hasNext());
        assertEquals(stub2, it$.next());
        assertFalse(it$.hasNext());
        it$.remove();
        assertEquals(1, list.size());
        assertTrue(list.contains(stub1));
        assertFalse(list.contains(stub2));
    }

    @Test
    public void testRemove() {
        final EntityStub stub1 = new EntityStub();
        final EntityStub stub2 = new EntityStub();
        list.add(stub1);
        list.add(stub2);
        list.remove(stub1);
        assertEquals(1, list.size());
        assertFalse(list.contains(stub1));
        assertTrue(list.contains(stub2));
        list.remove(stub2);
        assertFalse(list.contains(stub2));
        assertTrue(list.isEmpty());
    }

    @SuppressWarnings("serial")
    @Test
    public void testRemoveAll() {
        final List<EntityStub> stubs = new ArrayList<>() {{
            add(new EntityStub());
            add(new EntityStub());
            add(new EntityStub());
        }};
        list.addAll(stubs);
        assertEquals(3, list.size());
        list.removeAll(stubs);
        assertEquals(0, list.size());
    }

    @SuppressWarnings("serial")
    @Test
    public void testRetainAll() {
        final EntityStub stub1 = new EntityStub();
        final EntityStub stub2 = new EntityStub();
        final EntityStub stub3 = new EntityStub();
        final EntityStub stub4 = new EntityStub();
        list.add(stub1);
        list.add(stub2);
        list.add(stub3);
        list.add(stub4);
        final List<EntityStub> stubs = new ArrayList<>() {{
            add(stub2);
            add(stub4);
        }};
        list.retainAll(stubs);
        assertEquals(2, list.size());
        assertTrue(list.contains(stub2));
        assertTrue(list.contains(stub4));
        assertFalse(list.contains(stub1));
        assertFalse(list.contains(stub3));
    }

    @Test
    public void testSize() {
        assertEquals(0, list.size());
        list.add(new EntityStub());
        assertEquals(1, list.size());
        list.add(new EntityStub());
        list.add(new EntityStub());
        list.add(new EntityStub());
        assertEquals(4, list.size());
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    public void testToArray() {
        final EntityStub stub1 = new EntityStub();
        final EntityStub stub2 = new EntityStub();
        list.add(stub1);
        list.add(stub2);
        final Entity[] expected = new Entity[]{stub1, stub2};
        final Entity[] actual = list.toArray();
        assertArrayEquals(actual, expected);
    }

    @Test
    public void testToArrayTArray() {
        final EntityStub stub1 = new EntityStub();
        final EntityStub stub2 = new EntityStub();
        list.add(stub1);
        list.add(stub2);
        final EntityStub[] expected = new EntityStub[]{stub1, stub2};
        final EntityStub[] actual = list.toArray(new EntityStub[0]);
        assertArrayEquals(actual, expected);
    }

    private static class EntityStub extends Entity {

        @Override
        public void removeFromRegion(final Region region) {

        }

        @Override
        public void addToRegion(final Region region) {

        }

        @Override
        public void inflictDamage(final int damage, final HitType type) {

        }

        @Override
        public int getClientIndex() {
            return 0;
        }

    }

}
