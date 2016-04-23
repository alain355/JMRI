package apps;

/** 
 * Check how FindBugs and annotations interact.  
 * <p>
 * Note: This deliberately causes FindBugs warnings!  Do not remove or annotate them!
 *       Instead, when past its useful point, just comment out the body of the
 *       class so as to leave the code examples present.
 * <p>
 * Tests Nonnull, Nullable and CheckForNull from both 
 * javax.annotation and edu.umd.cs.findbugs.annotations
 * packages.
 * <p>
 * Comments indicate observed (and unobserved) warnings from FindBugs 3.0.1
 * <p>
 * This has no main() because it's not expected to run:  It will certainly
 * throw a NullPointerException right away.  The idea is for FindBugs to 
 * find those in static analysis
 * <p>
 * Types are explicitly qualified (instead of using 'import') to make it 
 * completely clear which is being used at each point.  That makes this the
 * code less readable, so it's not recommended for general use.
 *
 * @author Bob Jacobsen 2016
 */
public class FindBugsCheck {
    
    void test() { // something that has to be executed on an object
        System.out.println("test "+this.getClass());
    }
    
    // Test Nonnull
    
    @javax.annotation.Nonnull public FindBugsCheck jaNonnullReturn() {
        return null; // (NP_NONNULL_RETURN_VIOLATION) may return null, but is declared @Nonnull
    }
    public void jaNonNullParm(@javax.annotation.Nonnull FindBugsCheck p) {
        p.test();
    }
    public void jaTestNonnull() {
        jaNonnullReturn().test();

        jaNonNullParm(this);
        jaNonNullParm(null);  // (NP_NONNULL_PARAM_VIOLATION) Null passed for non-null parameter
        
        jaNonNullParm(jaNonnullReturn());
        jaNonNullParm(jaNullableReturn()); // should be flagged?
        jaNonNullParm(jaCheckForNullReturn()); // definitely should be flagged!
    }

    @edu.umd.cs.findbugs.annotations.NonNull public FindBugsCheck fbNonnullReturn() {
        return null; // (NP_NONNULL_RETURN_VIOLATION) may return null, but is declared @NonNull
    }
    public void fbNonNullParm(@edu.umd.cs.findbugs.annotations.NonNull FindBugsCheck p) {
        p.test();
    }
    public void fbTestNonnull() {
        fbNonnullReturn().test();

        fbNonNullParm(this);
        fbNonNullParm(null); // (NP_NONNULL_PARAM_VIOLATION) Null passed for non-null parameter  
        
        fbNonNullParm(fbNonnullReturn());
        fbNonNullParm(fbNullableReturn()); // should be flagged?
        fbNonNullParm(fbCheckForNullReturn()); // definitely should be flagged!
    }


    // Test Nullable
    
    @javax.annotation.Nullable public FindBugsCheck jaNullableReturn() {
        return null;
    }
    public void jaNullableParm(@javax.annotation.Nullable FindBugsCheck p) {
        p.test(); // isn't flagged
    }
    public void jaTestNullable() {
        jaNullableReturn().test(); // isn't flagged

        jaNullableParm(this);
        jaNullableParm(null);
        
        jaNullableParm(jaNonnullReturn());
        jaNullableParm(jaNullableReturn());
        jaNullableParm(jaCheckForNullReturn());
    }

    @edu.umd.cs.findbugs.annotations.Nullable public FindBugsCheck fbNullableReturn() {
        return null;
    }
    public void fbNullableParm(@edu.umd.cs.findbugs.annotations.Nullable FindBugsCheck p) {
        p.test(); // isn't flagged
    }
    public void fbTestNullable() {
        fbNullableReturn().test(); // isn't flagged

        fbNullableParm(this);
        fbNullableParm(null);
        
        fbNullableParm(fbNonnullReturn());
        fbNullableParm(fbNullableReturn());
        fbNullableParm(fbCheckForNullReturn());
    }


    // Test CheckForNull

    @javax.annotation.CheckForNull public FindBugsCheck jaCheckForNullReturn() {
        return null;
    }
    public void jaCheckForNullParm(@javax.annotation.CheckForNull FindBugsCheck p) {
        p.test();  // should be flagged!
    }
    public void jaTestCheckForNull() {
        jaCheckForNullReturn().test(); // (NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE) Possible null pointer dereference in .. due to return value of called method

        jaCheckForNullParm(this);
        jaCheckForNullParm(null);
        
        jaCheckForNullParm(jaNonnullReturn());
        jaCheckForNullParm(jaNullableReturn());
        jaCheckForNullParm(jaCheckForNullReturn());
    }
    
    @edu.umd.cs.findbugs.annotations.CheckForNull public FindBugsCheck fbCheckForNullReturn() {
        return null;
    }
    public void fbCheckForNullParm(@edu.umd.cs.findbugs.annotations.CheckForNull FindBugsCheck p) {
        p.test(); // should be flagged!
    }
    public void fbTestCheckForNull() {
        fbCheckForNullReturn().test(); // (NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE) Possible null pointer dereference in .. due to return value of called method

        fbCheckForNullParm(this);
        fbCheckForNullParm(null);
        
        fbCheckForNullParm(fbNonnullReturn());
        fbCheckForNullParm(fbNullableReturn());
        fbCheckForNullParm(fbCheckForNullReturn());
    }

}