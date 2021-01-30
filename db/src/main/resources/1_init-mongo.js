var dbo = db.getSiblingDB('admin');
dbo.createUser(
  {
    user : "app-user",
    pwd : "541259",
    roles: [
      { role: "userAdminAnyDatabase", db: "admin" },
      "readWriteAnyDatabase"
    ]
  }
);
dbo = db.getSiblingDB('tb-test');
dbo.createUser(
  {
    user : "app-user",
    pwd : "541259",
    roles: [
      { role: "readWrite", db: "tb-test" },
    ]
  }
);