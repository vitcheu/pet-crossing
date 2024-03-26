package com.vitcheu.common.constants.api;

public class ResourcePaths {

  public static final String API = "api";

  public static final String V1 = "/v1";
  public static final String V2 = "/v2";

  public static final String ID = "/id/{id}";
  public static final String USERNAME = "/username/{username}";
  public static final String PAGEABLE = "/pageable";
  public static final String GENDER = "/gender/{gender}";

  public static final String ROOT_API =
    "/" + ResourcePaths.API;/*   /api                        */
  public static final String ROOT_API_V1 =
    ResourcePaths.ROOT_API +
    ResourcePaths.V1;/*   /api/v1                     */

  public class Authentication {

    public static final String NAME = "/auth";

    public final class V1 {

      private V1() {}

      public static final String ROOT =
        // ResourcePaths.ROOT_API_V1 + Authentication.NAME;
          ResourcePaths.V1;

      public static final String SIGNUP = "/signup";
      public static final String VERIFICATION = "/verification/{token}";
      public static final String LOGIN = "/login";
      public static final String REFRESH_TOKEN = "/refresh/token";
      public static final String LOGOUT = "/logout";
    }
  }

  public class User {

    public static final String NAME = "/user";

    public final class V1 {

      private V1() {}

      public static final String ROOT =
        ResourcePaths.ROOT_API_V1 + User.NAME;/*	/api/v1/user                */
    }
  }

  public class PersonManagement {

    public static final String NAME = "/management/person";

    public final class V1 {

      private V1() {}

      public static final String ROOT =
        ResourcePaths.ROOT_API_V1 +
        PersonManagement.NAME;/*	/api/v1/management/person   */
    }
  }

  public class Person {

    public static final String NAME = "/person";

    public final class V1 {

      private V1() {}

      public static final String ROOT =
        ResourcePaths.ROOT_API_V1 + Person.NAME;/*	/api/v1/person              */

      public static final String GET =
        ResourcePaths.ROOT_API_V1 +
        Person.NAME +
        ResourcePaths.ID;/*	/api/v1/person/id           */

      public static final String GET_BY_GENDER = ResourcePaths.GENDER;

      public static final String PAGEABLE = ResourcePaths.GENDER;
    }
  }

  public class SBAT {

    public static final String NAME = "/sbat";

    public final class V1 {

      private V1() {}

      public static final String ROOT = SBAT.NAME;

      public static final String PERSONS = "/persons";
      public static final String INDEX = "/index";
      public static final String LOGIN = "/login";
      public static final String ABOUT = "/about";
      public static final String SETTINGS = "/settings";
      public static final String TECH_STACK = "/tech-stack";
      public static final String CLOSE = "/close";
      public static final String ARCHITECTURE = "/architecture";
      public static final String DOCKER = "/docker";
      public static final String STATUS = "/status";
      public static final String SECURITY = "/security";
    }
  }
}
