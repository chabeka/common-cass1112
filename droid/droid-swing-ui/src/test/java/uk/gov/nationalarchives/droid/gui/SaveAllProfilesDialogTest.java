/**
 * Copyright (c) 2012, The National Archives <pronom@nationalarchives.gsi.gov.uk>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following
 * conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of the The National Archives nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package uk.gov.nationalarchives.droid.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Test;

import uk.gov.nationalarchives.droid.gui.widgetwrapper.ProfileSelectionDialog;

public class SaveAllProfilesDialogTest {

   static {
      System.setProperty("java.awt.headless", "true");
   }

   @Test
   @Ignore
   public void showGui() throws Exception {

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

      ProfileForm profile1 = mock(ProfileForm.class);
      ProfileForm profile2 = mock(ProfileForm.class);

      when(profile1.getName()).thenReturn("profile1");
      when(profile2.getName()).thenReturn("profile2");

      List<ProfileForm> profiles = new ArrayList<ProfileForm>();
      profiles.add(profile1);
      profiles.add(profile2);

      ProfileSelectionDialog dialog = new SaveAllProfilesDialog(null, profiles);

      dialog.open();
   }

   @Test
   @Ignore("nécessite java.awt.headless=true")
   public void testSelectionWhenProfile2IsUnselected() {
      ProfileForm profile1 = mock(ProfileForm.class);
      ProfileForm profile2 = mock(ProfileForm.class);

      when(profile1.getName()).thenReturn("profile1");
      when(profile2.getName()).thenReturn("profile2");

      List<ProfileForm> profiles = new ArrayList<ProfileForm>();
      profiles.add(profile1);
      profiles.add(profile2);

      SaveAllProfilesDialog dialog = new SaveAllProfilesDialog(null, profiles);
      dialog.getModel().get(1).toggleSelection();

      List<ProfileForm> selections = dialog.getSelectedProfiles();
      assertTrue(selections.contains(profile1));
      System.out.println("$*$: " + selections.contains(profile1));
      assertFalse(selections.contains(profile2));
      System.out.println("$*$: " + selections.contains(profile2));
   }

   @Test
   @Ignore("nécessite java.awt.headless=true")
   public void testSelectionWhenBothProfilesAreUnselected() {
      ProfileForm profile1 = mock(ProfileForm.class);
      ProfileForm profile2 = mock(ProfileForm.class);

      when(profile1.getName()).thenReturn("profile1");
      when(profile2.getName()).thenReturn("profile2");

      List<ProfileForm> profiles = new ArrayList<ProfileForm>();
      profiles.add(profile1);
      profiles.add(profile2);

      SaveAllProfilesDialog dialog = new SaveAllProfilesDialog(null, profiles);
      dialog.getModel().get(0).toggleSelection();
      dialog.getModel().get(1).toggleSelection();

      List<ProfileForm> selections = dialog.getSelectedProfiles();
      assertFalse(selections.contains(profile2));
      assertFalse(selections.contains(profile1));
      assertTrue(selections.isEmpty());
   }
}