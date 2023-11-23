package km1.algafood.domain.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class UserTests {
  @Test
  void testAddGroup() {
    var underTest = User.builder().build();
    var group = Group.builder().build();
    assertTrue(underTest.addGroup(group));
    assertTrue(underTest.getGroups().contains(group));
  }

  @Test
  void testPasswordMatches() {
    var password = "kaique";
    var underTest = User.builder().password(password).build();
    assertTrue(underTest.passwordMatches(password));
  }

  @Test
  void testPasswordMatches2() {
    var password = "kaique";
    var underTest = User.builder().password(password).build();
    password = "kaique2";
    assertFalse(underTest.passwordMatches(password));
  }
  @Test
  void testPasswordNotMatches() {
    var password = "kaique";
    var underTest = User.builder().password(password).build();
    assertFalse(underTest.passwordNotMatches(password));
  }

  @Test
  void testPasswordNotMatches1() {
    var password = "kaique";
    var underTest = User.builder().password(password).build();
    password = "kaique2";
    assertTrue(underTest.passwordNotMatches(password));
  }

  @Test
  void testRemoveGroup() {
    var group = Group.builder().build();
    var groups = new HashSet<Group>();
    groups.add(group);
    var underTest = User.builder().groups(groups).build();
    assertTrue(underTest.removeGroup(group));
    assertFalse(underTest.getGroups().contains(group));
  }
}
