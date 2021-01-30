var dbo = db.getSiblingDB('tb-test');
dbo.tanks.insertMany([
  { id : 1, name : "Panzer IV", tread : "STABLE",  health : 5, position : { x : 0, y : 0 } },
  { id : 2, name : "T-34", tread : "STABLE", health : 5, position : { x : 0, y : 0 } }
]);