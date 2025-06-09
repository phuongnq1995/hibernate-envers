package org.phuongnq.hibernate_envers.config.audit;

public class CurrentModuleAudit {
  public static final CurrentModuleAudit INSTANCE = new CurrentModuleAudit();

  private static final ThreadLocal<String> storage = new ThreadLocal<>();

  public void setModule(String user) {
    storage.set(user);
  }

  public void remove() {
    storage.remove();
  }

  public String get() {
    return storage.get();
  }
}
