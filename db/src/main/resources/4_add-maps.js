var dbo = db.getSiblingDB('tb-test');
dbo.maps.insertOne({
  id : "1",
  fieldX : 100,
  fieldY : 80,
  startPointA : {
    x : 1,
    y : 40
  },
  startPointB : {
    x : 100,
    y : 40
  },
  obstacles : [
    {
      blocksMove : true,
      damage : 0,
      position : {
        x : 10,
        y : 20
      }
    },
    {
      blocksMove : false,
      damage : 1,
      position : {
        x : 30,
        y : 40
      }
    },
    {
      blocksMove : true,
      damage : 0,
      position : {
        x : 61,
        y : 80
      }
    },
    {
      blocksMove : false,
      damage : 1,
      position : {
        x : 10,
        y : 5
      }
    },
    {
      blocksMove : true,
      damage : 0,
      position : {
        x : 70,
        y : 40
      }
    },
    {
      blocksMove : true,
      damage : 0,
      position : {
        x : 40,
        y : 40
      }
    },
    {
      blocksMove : false,
      damage : 1,
      position : {
        x : 30,
        y : 60
      }
    },
    {
      blocksMove : true,
      damage : 0,
      position : {
        x : 45,
        y : 55
      }
    },
    {
      blocksMove : true,
      damage : 0,
      position : {
        x : 17,
        y : 24
      }
    },
    {
      blocksMove : false,
      damage : 1,
      position : {
        x : 90,
        y : 40
      }
    }
  ]
});