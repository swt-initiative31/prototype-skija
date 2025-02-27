package org.eclipse.swt.tests.skija;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ Test_org_eclipse_swt_graphics_SkijaGC.class })
public class AllSkijaTests {

    public static void main(String[] args) {
	JUnitCore.main(AllSkijaTests.class.getName());
    }

}
