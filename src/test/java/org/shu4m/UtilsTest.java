/* 
 * Copyright 2013 Matthias Huebner
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.shu4m;

import static org.junit.Assert.fail;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

//  @Test
//  public void testPackageToPath() {
//    fail("Not yet implemented");
//  }

//  @Test
//  public void testClean() {
//    fail("Not yet implemented");
//  }

//  @Test
//  public void testFormatDate() {
//    fail("Not yet implemented");
//  }

//  @Test
//  public void testMakeJavaName() {
//    fail("Not yet implemented");
//  }

//  @Test
//  public void testMakeDir() {
//    fail("Not yet implemented");
//  }

//  @Test
//  public void testClose() {
//    fail("Not yet implemented");
//  }

//  @Test
//  public void testExecuteSetVersion() {
//    fail("Not yet implemented");
//  }

  @Test
  public void testFormatVersion() {
    Assert.assertEquals("1.0.3", Utils.formatVersion(new Version(1, 0, 3)));
    Assert.assertEquals("1.0.3-123", Utils.formatVersion(new Version(1, 0, 3, 123)));
    Assert.assertEquals("1.0.3-BETA3", Utils.formatVersion(new Version(1, 0, 3, "BETA3")));
  }

  @Test
  public void testIsNotBlank() {
    Assert.assertTrue(Utils.isNotBlank("12  "));
    Assert.assertFalse(Utils.isNotBlank("  "));
    Assert.assertFalse(Utils.isNotBlank(null));
  }

  @Test
  public void testParseVersion() throws MojoExecutionException {
    verify(Utils.parseVersion("1.0.1"), 1, 0, 1);
    verify(Utils.parseVersion("1.2.3-BETA1"), 1, 2, 3, "BETA1");
    verify(Utils.parseVersion("1.0.1-133"), 1, 0, 1, 133);
  }

  @Test(expected = MojoExecutionException.class)
  public void testParseVersion1() throws MojoExecutionException {
    Utils.parseVersion("1.0");
  }

  @Test(expected = MojoExecutionException.class)
  public void testParseVersion2() throws MojoExecutionException {
    Utils.parseVersion("1.a-123");
  }

  @Test(expected = MojoExecutionException.class)
  public void testParseVersion3() throws MojoExecutionException {
    Utils.parseVersion("1.1-123a");
  }

  private void verify(Version version, int major, Integer minor, Integer incremental) {
    Assert.assertEquals(version.getMajor(), major);
    Assert.assertEquals(version.getMinor(), minor);
    Assert.assertEquals(version.getIncremental(), incremental);
    Assert.assertNull(version.getQualifier());
    Assert.assertNull(version.getBuild());
  }

  private void verify(Version version, int major, Integer minor, Integer incremental, String qualifier) {
    Assert.assertEquals(version.getMajor(), major);
    Assert.assertEquals(version.getMinor(), minor);
    Assert.assertEquals(version.getIncremental(), incremental);
    Assert.assertEquals(version.getQualifier(), qualifier);
    Assert.assertNull(version.getBuild());
  }

  private void verify(Version version, int major, Integer minor, Integer incremental, Integer build) {
    Assert.assertEquals(version.getMajor(), major);
    Assert.assertEquals(version.getMinor(), minor);
    Assert.assertEquals(version.getIncremental(), incremental);
    Assert.assertNull(version.getQualifier());
    Assert.assertEquals(version.getBuild(), build);
  }
}
