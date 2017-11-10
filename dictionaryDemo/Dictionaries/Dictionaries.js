var dict;
function setup() {
  createCanvas(1600, 800);
  dict = new Dictionary();
  dict.add("dog", "embwa");
  dict.add("chair", "entebbe");
  dict.add("luck", "ekisa");
  background(105, 109, 125);  
  textSize(25);
  fill(250,250,250);
  text(dict.printKeys(),250,100);
}

function draw() {
  strokeWeight(5);
  stroke(141, 159, 135);
  fill(205, 198, 165);
  strokeWeight(10);
  stroke(205, 198, 165);
  fill(240, 220, 202);
  rect(30, 10, 195, 100);
  fill(0, 0, 0);
  noStroke();  

  text("TRANSLATOR", 40, 40);
  if (mouseIsPressed)
  {  fill(250,250,250);

     text(dict.showAll(), 250, 100);

  }
}

  function Dictionary() {
    this.add = add;
    this.datastore ={};
    this.find = find;
    this.remove = remove;
    this.showAll = showAll;
    this.printKeys = printKeys;
  }
  function add(key, value) {
    this.datastore[key] = value;
  }
  function find(key) {
    return this.datastore[key];
  }
  function remove(key)
  {
    delete this.datastore[key];
  }
  function showAll()
  { 
    var output ="";
    for (var key in this.datastore)
    {
      output+=key+" -> "+this.datastore[key]+"\n";
    }
    print(output);
    return output;
  }
  function count() {
    var n = 0;
    for (var key in this.datastore) {
      ++n;
    }
    return n;
  }
  function clear() {
    for (var key in this.datastore) {
      delete this.datastore[key];
    }
  }
  function printKeys() {
  var output = "";
    for (var key in this.datastore)
    {
      output+=key+"\n";
    }
    return output;
  }