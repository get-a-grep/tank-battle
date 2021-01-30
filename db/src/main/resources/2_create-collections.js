var dbo = db.getSiblingDB('tb-test');
dbo.createCollection("tanks", function(err, res) {
  if (err) throw err;
  console.log("Tanks collection created...");
});
dbo.createCollection("maps", function(err, res) {
  if (err) throw err;
  console.log("Maps collection created...");
});
dbo.createCollection("games", function(err, res) {
  if (err) throw err;
  console.log("Games collection created, closing connection.");
  db.close();
});