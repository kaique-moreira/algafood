package km1.algafood.domain.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class GroupTests {
  @Test
  void testAddPermission() {
    var underTest = Group.builder().build();
    var permission = Permission.builder().build();
    assertTrue(underTest.addPermission(permission));
    assertTrue(underTest.getPermissions().contains(permission));
  }

  @Test
  void testRemovePermission() {
    var permission = Permission.builder().build();
    Set<Permission> set = new HashSet<Permission>();
    set.add(permission);

    var underTest = Group.builder().permissions(set).build();
    assertTrue(underTest.removePermission(permission));
    assertFalse(underTest.getPermissions().contains(permission));
  }
}
