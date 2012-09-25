package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

import org.bukkit.craftbukkit.util.UnsafeList;

public class EntityAITasks {

    // CraftBukkit start - ArrayList -> UnsafeList
    private List a = new UnsafeList();
    private List b = new UnsafeList();
    // CraftBukkit end
    private final Profiler c;
    private int d = 0;
    private int e = 3;

    public EntityAITasks(Profiler methodprofiler) {
        this.c = methodprofiler;
    }

    public void a(int i, EntityAIBase pathfindergoal) {
        this.a.add(new EntityAITaskEntry(this, i, pathfindergoal));
    }

    public void a() {
        // ArrayList arraylist = new ArrayList(); // CraftBukkit - remove usage
        Iterator iterator;
		EntityAITaskEntry pathfindergoalselectoritem;

        if (this.d++ % this.e == 0) {
            iterator = this.a.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselectoritem = (EntityAITaskEntry) iterator.next();
                boolean flag = this.b.contains(pathfindergoalselectoritem);

                if (flag) {
                    if (this.b(pathfindergoalselectoritem) && this.a(pathfindergoalselectoritem)) {
                        continue;
                    }

                    pathfindergoalselectoritem.a.c();
                    this.b.remove(pathfindergoalselectoritem);
                }

                if (this.b(pathfindergoalselectoritem) && pathfindergoalselectoritem.a.a()) {
                    // CraftBukkit start - call method now instead of queueing
                    // arraylist.add(pathfindergoalselectoritem);
                    pathfindergoalselectoritem.a.e();
                    // CraftBukkit end
                    this.b.add(pathfindergoalselectoritem);
                }
            }
        } else {
            iterator = this.b.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselectoritem = (EntityAITaskEntry) iterator.next();
                if (!pathfindergoalselectoritem.a.b()) {
                    pathfindergoalselectoritem.a.c();
                    iterator.remove();
                }
            }
        }

        // this.c.a("goalStart"); // CraftBukkit - not in production code
        // CraftBukkit start - removed usage of arraylist
        /*iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
            // this.c.a(pathfindergoalselectoritem.a.getClass().getSimpleName()); // CraftBukkit - not in production code
            pathfindergoalselectoritem.a.e();
            // this.c.b(); // CraftBukkit - not in production code
        }*/
        // CraftBukkit end

        // this.c.b(); // CraftBukkit - not in production code
        // this.c.a("goalTick"); // CraftBukkit - not in production code
        iterator = this.b.iterator();

        while (iterator.hasNext()) {
            pathfindergoalselectoritem = (EntityAITaskEntry) iterator.next();
            // this.c.a(pathfindergoalselectoritem.a.getClass().getSimpleName()); // CraftBukkit - not in production code
            pathfindergoalselectoritem.a.d();
            // this.c.b(); // CraftBukkit - not in production code
        }

        // this.c.b(); // CraftBukkit - not in production code
    }

    private boolean a(EntityAITaskEntry pathfindergoalselectoritem) {
        // this.c.a("canContinue"); // CraftBukkit - not in production code
        boolean flag = pathfindergoalselectoritem.a.b();

        // this.c.b(); // CraftBukkit - not in production code
        return flag;
    }

    private boolean b(EntityAITaskEntry pathfindergoalselectoritem) {
        // this.c.a("canUse"); // CraftBukkit - not in production code
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
			EntityAITaskEntry pathfindergoalselectoritem1 = (EntityAITaskEntry) iterator.next();

            if (pathfindergoalselectoritem1 != pathfindergoalselectoritem) {
                if (pathfindergoalselectoritem.b >= pathfindergoalselectoritem1.b) {
                    // CraftBukkit - switch order
                    if (!this.a(pathfindergoalselectoritem, pathfindergoalselectoritem1) && this.b.contains(pathfindergoalselectoritem1)) {
                        // this.c.b(); // CraftBukkit - not in production code
                        ((UnsafeList.Itr) iterator).valid = false; // CraftBukkit - mark iterator for reuse
                        return false;
                    }
                // CraftBukkit - switch order
                } else if (!pathfindergoalselectoritem1.a.g() && this.b.contains(pathfindergoalselectoritem1)) {
                    // this.c.b(); // CraftBukkit - not in production code
                    ((UnsafeList.Itr) iterator).valid = false; // CraftBukkit - mark iterator for reuse
                    return false;
                }
            }
        }

        // this.c.b(); // CraftBukkit - not in production code
        return true;
    }

    private boolean a(EntityAITaskEntry pathfindergoalselectoritem, EntityAITaskEntry pathfindergoalselectoritem1) {
        return (pathfindergoalselectoritem.a.h() & pathfindergoalselectoritem1.a.h()) == 0;
    }
}
