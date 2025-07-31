<p align="center">
  <img alt="Duzzy, Give me your schema, I'll give you your test data." src="docs/images/banner.png">
</p>
<h1 align="center">
  Give me your schema, <br />I'll give you your test data.
</h1>

![Duzzy check](https://github.com/bhagenbourger/duzzy/actions/workflows/main.yml/badge.svg)
![GitHub License](https://img.shields.io/github/license/bhagenbourger/duzzy?color=%234169E1)

Duzzy is a data generation tool that aims to facilitate the generation of test data.  
Duzzy is designed to be as simple as possible, able to run without any parameters (which means that every parameter has a default value) but fully configurable and expandable: easy to start up, easy to tune.

## 🤔 Why Duzzy?
>"Do you have a dataset to test this pipeline?"  
>"I need data to test my job."  
>"I can't reproduce this bug without data."  
>"Do you know how to publish a relevant message into this Kafka topic?"  
>"Do you have a parquet file to test that case?"  

Have you ever heard these sentences, or perhaps you have already said them?

Generating data shouldn't be a complicated, time-consuming or boring task.  
Duzzy aims to solve these problems and provide a no-brainer way to generate data to meet your need.  
So now, when you need data: duzzy it!

## 🚀 Getting started
♨️ Duzzy is built with Java 21, so you need java 21 to run it. ♨️

### Install latest version
```
DUZZY_VERSION=0.0.0
wget "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/duzzy-${DUZZY_VERSION}.zip"
unzip duzzy-${DUZZY_VERSION}.zip
```

### Run it
```
"duzzy-${DUZZY_VERSION}/bin/duzzy run"
```
Output:
```
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"39Y2jkN7sey","DoubleRandomProvider":3.4443151307062914E306}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"tJlhVs5kobcEH4","DoubleRandomProvider":7.00263593450126E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"NQzUZeivqbgkNGN","DoubleRandomProvider":4.919350633385859E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"CtvWjl2Rv3BP5","DoubleRandomProvider":9.895943522452931E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"7RWhYpSFRP5J8t","DoubleRandomProvider":8.99398384636745E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"GPuJeZNQfNa3taJ","DoubleRandomProvider":8.414529311331451E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"rnmqEiRrrJyINe","DoubleRandomProvider":4.144963844067495E306}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"2T1p7OdgO9pMy","DoubleRandomProvider":4.381784483832912E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"lKp2hWWtjqxHtDO","DoubleRandomProvider":1.677140353287274E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"SOiQ5bTB9H","DoubleRandomProvider":1.7836945653614943E308}
DuzzyResult[totalDuration=PT0.041S, processingDuration=PT0.032S, rows=10, size=1221, seed=2285108886562336170]
```

> 💡 In examples below, `"duzzy-${DUZZY_VERSION}/bin/duzzy"` is replaced by `duzzy`.

### Print help
```
duzzy -h
```
Output:
```
Usage: duzzy [-hV] [COMMAND]
Give me your schema, I'll give you your test data.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  run     Generate your test data
  plugin  Manage your plugins
```

### Print help for duzzy run
```
duzzy run -h
```
Output:
```
Usage: duzzy run [-hV] [-c=File] [-d=Long] [-f=File] [-o=OutputFormat]
                 [-p=Class] [-r=Long] [-s=Long] [-t=Integer] [-z=Long]
Generate your test data
  -c, --config-file=File   Config file used to enrich the schema
  -d, --duration=Long      Duration of data generation in milliseconds (per
                             thread), default is unlimited
  -f, --schema-file=File   Schema source file
  -h, --help               Show this help message and exit.
  -o, --output=OutputFormat
                           Output format, supported values: RAW, TXT, JSON,
                             XML, YAML
  -p, --schema-parser=Class
                           Qualified name of the parser class used to parse
                             schema file
  -r, --rows=Long          Number of rows to generate (per thread), default is
                             10
  -s, --seed=Long          Seed used to generate data
  -t, --threads=Integer    Number of threads to use to generate data
  -V, --version            Print version information and exit.
  -z, --size=Long          Size of data to generate in bytes (per thread),
                             default is unlimited
```

### Print help for duzzy plugin
```
duzzy plugin -h
```
Output:
```
Usage: duzzy plugin [-hV] [COMMAND]
Manage your plugins
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  install    Install a plugin
  list       List all installed plugins
  uninstall  Uninstall a plugin
```
### Set limits
> 💡 When several limits are set, Duzzy stops when one limit is reached.

> 💡Generated rows are always full, 
> meaning Duzzy doesn't stop strictly at the but limit when the limit is reached or exceeded

#### Set number of rows to generate
```
duzzy run -r 5
```
Output:
```
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"mL1O47KvHIYKJTy","DoubleRandomProvider":7.649987037298262E306}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"Lvjrthzv9x","DoubleRandomProvider":1.538213261065545E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"fFc33wsMUX","DoubleRandomProvider":1.3510109778635097E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"broNTgG7azwI860","DoubleRandomProvider":1.1489755225447667E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"9WIhfsRcjeHZlu","DoubleRandomProvider":1.3599574206137938E308}
DuzzyResult[totalDuration=PT0.051S, processingDuration=PT0.04S, rows=5, size=608, seed=-4594484013830885479]
```

#### Set size (in bytes) of date to generate
```
duzzy run -z 1000
```
Output:
```
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"7uP3MtPAcnPD42r","DoubleRandomProvider":1.426055674820472E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"mzkjSbpjyr6pZO","DoubleRandomProvider":3.7422019071188695E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"s0L9HGVDi70","DoubleRandomProvider":3.260206849504213E306}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"WsroTMJWH8Vm","DoubleRandomProvider":1.256759653429498E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"qHVF7Day3J2","DoubleRandomProvider":2.0944668090326934E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"kVc1iXy3hHCEZ","DoubleRandomProvider":1.5512212336044818E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"M8M1oi4kV06qg","DoubleRandomProvider":4.024856998275804E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"DOpLzTkm57v","DoubleRandomProvider":1.4789516023754013E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"RFF3n69Sey","DoubleRandomProvider":1.586086361927003E307}
DuzzyResult[totalDuration=PT0.042S, processingDuration=PT0.032S, rows=9, size=1087, seed=-7508667141858130218]
```

#### Set generation duration (in seconds)
> 💡 Duration limit is based on the time spent to processing data. 
> Initialization time and closing time are not counted in the duration limit.
```
duzzy run -d 1
```
Output:
```
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"Ij2jHBVm6uXAdGT","DoubleRandomProvider":1.3827536102424086E308}
[...]
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"9XnqdDh94Mj1UB","DoubleRandomProvider":1.0963850066868554E308}
DuzzyResult[totalDuration=PT9.858S, processingDuration=PT1S, rows=469183, size=56969953, seed=1942390080647646563]
```

### Use seed to have idempotent result
```
duzzy run -s 1234
```
Output:
```
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"7M95YxRzOBlpZ","DoubleRandomProvider":7.606673968552325E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"xIJnpkufuykcq","DoubleRandomProvider":4.137192418713065E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"Nir5lwHHUkC9","DoubleRandomProvider":1.305580678149016E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"Tykw0HhqM1b","DoubleRandomProvider":1.0937786695091996E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"3bacagIKM760IS","DoubleRandomProvider":7.147258844333934E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"XrafBR3LDeMlwZp","DoubleRandomProvider":5.96564756351993E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"JbPT4VHoqYpdt3P","DoubleRandomProvider":9.271277991289887E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"22GAbWyGa5JZkrA","DoubleRandomProvider":2.1335952966233372E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"wiK8MCBWHnoE","DoubleRandomProvider":1.2138137952960131E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"7wqE5d72SdC","DoubleRandomProvider":1.0306199098126328E307}
DuzzyResult[totalDuration=PT0.044S, processingDuration=PT0.033S, rows=10, size=1220, seed=1234]
```

### Specify a sink
By default, Duzzy print result into the console, you can specify another `sink` using a `DuzzyConfig` file.   
Below an example of `DuzzyConfig` to use a local file `sink` that will generate a local file name `/tmp/example.json` with your data:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.LocalFileSink"
  filename: "/tmp/example.json"
```
Copy/paste the content above into a file name `duzzy-config.yaml`.  
Run duzzy:
```
duzzy run -c duzzy-config.yaml
```
Output:
```
DuzzyResult[totalDuration=PT0.031S, processingDuration=PT0.017S, rows=10, size=1211, seed=3516556014612409473]
```

Check `/tmp/example.json  ` file content.
```
cat /tmp/example.json  
```
Output:
```                         
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"01kNqdylTpxRP","DoubleRandomProvider":6.468097762224575E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"cShwZVy5zzmU5","DoubleRandomProvider":3.7837076971762515E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"K0KJJx2fHvi","DoubleRandomProvider":8.898142971794124E305}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"5J2s1TDFdk","DoubleRandomProvider":1.7592431490301122E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"dT4O9z09A4","DoubleRandomProvider":1.2386013716796864E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"7imKzOwzw8rA","DoubleRandomProvider":3.5593048703828705E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"Gks0D30ig0Bv","DoubleRandomProvider":1.2569898826582618E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"3n7lpj6HNCH","DoubleRandomProvider":2.1648875903342025E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"E7auSmaOWCGP","DoubleRandomProvider":9.294922043959701E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"agibjBDHEQa1THm","DoubleRandomProvider":5.005957353003563E307}
```

### Specify a serializer
By default, Duzzy format result in JSON, you can specify another `serializer` using a `DuzzyConfig` file.   
Below an example of `DuzzyConfig` to use a XML `serializer`:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.ConsoleSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.XmlSerializer"
    root_tag: myRootTag
    row_tag: myRowTag
```
Copy/paste the content above into a file name `duzzy-config.yaml`.  
Run duzzy:
```
duzzy run -c duzzy-config.yaml
```
Output:
```
<?xml version='1.0' encoding='UTF-8'?><myRootTag><myRowTag><BooleanRandomProvider>true</BooleanRandomProvider><AlphanumericRandomProvider>JBJ2fDwddkVu</AlphanumericRandomProvider><DoubleRandomProvider>4.2953995065998953E307</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>DwWvoz2Vdqi</AlphanumericRandomProvider><DoubleRandomProvider>1.6390261767795804E308</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>8G9qSKVmAPc4Ugn</AlphanumericRandomProvider><DoubleRandomProvider>6.798437676547761E307</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>8PhhqFzPV3kWi</AlphanumericRandomProvider><DoubleRandomProvider>1.5473839366108578E308</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>ZdfOjZqmXq</AlphanumericRandomProvider><DoubleRandomProvider>1.0469257511619842E307</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>true</BooleanRandomProvider><AlphanumericRandomProvider>JhHRWwSjBchYqpf</AlphanumericRandomProvider><DoubleRandomProvider>6.465908728369437E307</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>t6S2fQHxFxr</AlphanumericRandomProvider><DoubleRandomProvider>1.452428137061351E308</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>AurkVMTwjvp</AlphanumericRandomProvider><DoubleRandomProvider>1.0858635476809697E308</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>ZwfumdNVJyRBL</AlphanumericRandomProvider><DoubleRandomProvider>3.9308768460020056E307</DoubleRandomProvider></myRowTag><myRowTag><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>BaZQGOpaNI9tqNz</AlphanumericRandomProvider><DoubleRandomProvider>1.5443249936260113E308</DoubleRandomProvider></myRowTag></myRootTag>
DuzzyResult[totalDuration=PT0.034S, processingDuration=PT0.025S, rows=10, size=2152, seed=-948642641714584412]
```

### Specify your own schema
By default, Duzzy generate 3 fields (BooleanRandomProvide, AlphanumericRandomProvider and DoubleRandomProvider), you can specify your own schema using a `DuzzySchema`.  
Below an example of `DuzzySchema`:
```
---
fields:
  - name: id
    type: INTEGER
    null_rate: 0
    corrupted_rate: 0
    providers:
      - identifier: "io.duzzy.plugin.provider.increment.IntegerIncrementProvider"
        start: 1
        step: 1
  - name: name
    type: STRING
    null_rate: 0
    corrupted_rate: 0
    providers:
      - identifier: "io.duzzy.plugin.provider.random.AlphanumericRandomProvider"
  - name: gender
    type: STRING
    null_rate: 0
    corrupted_rate: 0
    providers:
      - identifier: "io.duzzy.plugin.provider.constant.StringListConstantProvider"
        values: [ "M", "F"]
```
Copy/paste the content above into a file name `duzzy-schema.yaml`.  
Run duzzy:
```
duzzy run -f duzzy-schema.yaml
```
Output:
```
{"id":1,"name":"PaODmvFKXmgmrDz","gender":"M"}
{"id":2,"name":"VWrRYn90K0NnnH1","gender":"M"}
{"id":3,"name":"TPowuuRHUp3tvL","gender":"M"}
{"id":4,"name":"Vc7vKrpY7G2","gender":"M"}
{"id":5,"name":"uqs98c9UrA7St","gender":"F"}
{"id":6,"name":"JG4rJSbJe9W","gender":"F"}
{"id":7,"name":"kfdYTS5qm6Z0LF","gender":"M"}
{"id":8,"name":"bWHrrIOuDcvIK","gender":"M"}
{"id":9,"name":"OplVgtnWC2R4I","gender":"M"}
{"id":10,"name":"tajxjEz1uF","gender":"F"}
DuzzyResult[totalDuration=PT0.022S, processingDuration=PT0.014S, rows=10, size=449, seed=-5630598479812439836]
```

Example using several options:
```
duzzy run -f duzzy-schema.yaml -c duzzy-config.yaml -s 1234 -r 5 -o TXT 
```
Output:
```
<?xml version='1.0' encoding='UTF-8'?><rows><row><id>1</id><name>i7M95YxRzOB</name><gender>M</gender></row><row><id>2</id><name>ZxIJnpkufuy</name><gender>M</gender></row><row><id>3</id><name>DNir5lwHHUkC</name><gender>M</gender></row><row><id>4</id><name>mTykw0HhqM1b</name><gender>F</gender></row><row><id>5</id><name>3bacagIKM760</name><gender>F</gender></row></rows>
Duzzy generated 5 rows in PT0.027S (processing time: PT0.019S) which represent 429 bytes of data with seed 1234
```

### Use threads for parallel processing
> 💡 Some sinks are mono thread, like `io.duzzy.plugin.sink.ConsoleSink`.

> 💡 When using threads, limits are considered by thread. Meaning that if you specify `-t 5` and `-r 10`, you will have 50 rows generated.

Below an example of `DuzzyConfig` to use a local file `sink` that will generate a local file name `/tmp/example.json` with your data:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.LocalFileSink"
  filename: "/tmp/multi/example.json"
  create_if_not_exists: true
```
Copy/paste the content above into a file name `duzzy-config.yaml`.  
Run duzzy:
```
duzzy run -c duzzy-config.yaml -t 5
```
Output:
```
DuzzyResult[totalDuration=PT0.043S, processingDuration=PT0.11S, rows=50, size=6045, seed=-6378206102201769831]
```
Five files are generated, one by thread.  
```
ll -1 /tmp/multi
```
Output:
```
example_26.json
example_27.json
example_28.json
example_29.json
example_30.json
```
Check data:
```
cat /tmp/mutli/example_26.json                               
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"jLlTePqWmYayHlb","DoubleRandomProvider":1.7154597102424195E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"1xHFwiylYyBIng","DoubleRandomProvider":1.7701769726667074E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"CSEcZ6SCTibIdp","DoubleRandomProvider":5.939848967045316E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"vv71NKkgF6rc","DoubleRandomProvider":8.125563769696271E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"KTMFo6Y540mV8bq","DoubleRandomProvider":4.0392807613766315E305}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"cohTGEMkkR","DoubleRandomProvider":1.7506907272739248E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"C6JaxS4jMXV","DoubleRandomProvider":1.7083509777266206E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"8AHtQYyFphZ","DoubleRandomProvider":7.179680183050588E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"Yt4piWxEgvlW","DoubleRandomProvider":1.2390040097715712E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"h5xBBThNn1","DoubleRandomProvider":7.749315404400667E307}

cat /tmp/mutli/example_27.json
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"qobDQ6p9iqg3P","DoubleRandomProvider":1.7745308196577455E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"gH3YYErqwu3HV","DoubleRandomProvider":9.55087052529657E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"9G0thO4G2m","DoubleRandomProvider":3.81099038978294E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"0xMun4WCoTmEQ","DoubleRandomProvider":8.758921593329242E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"e8B0A2b0bv","DoubleRandomProvider":9.308817596115473E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"u4Wi3qr9Sz","DoubleRandomProvider":1.5256764410241623E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"2GApg9GWzc","DoubleRandomProvider":1.3737627053084292E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"aNj2dzgmmr","DoubleRandomProvider":1.2728002494040472E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"vgT1TOoeP2G","DoubleRandomProvider":6.734504602548767E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"M251HeH6OMz","DoubleRandomProvider":8.639501992218329E307}

cat /tmp/mutli/example_28.json
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"3K8koFAVom5xlxB","DoubleRandomProvider":1.5150176548873537E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"9Ru1opxSuh","DoubleRandomProvider":2.0463056908836394E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"D9c5HgsDxUznJ","DoubleRandomProvider":1.1305910785855872E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"7cY3u18EF7hz","DoubleRandomProvider":1.3758399653203094E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"LFP5QbPCz3Oq","DoubleRandomProvider":6.072274759114535E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"Fv3SdRfAI9Lg","DoubleRandomProvider":3.3077091510471404E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"sR1JH04BNg2","DoubleRandomProvider":6.746534075706819E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"dgDwCE6FikqZEHC","DoubleRandomProvider":1.1635622620420027E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"8BnRM9rtxe9","DoubleRandomProvider":1.4011992132248453E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"B31BVJrRps","DoubleRandomProvider":1.7551604193421899E308}

cat /tmp/mutli/example_29.json
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"4B82nkcTEP","DoubleRandomProvider":1.3355257488347639E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"WRVF3QpGPap","DoubleRandomProvider":1.5924435793854302E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"2xtJECDcXVm","DoubleRandomProvider":1.6360720290156174E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"kyQn6jfoA7jvPA","DoubleRandomProvider":6.586957816097621E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"hlv1lTiQ36","DoubleRandomProvider":1.7154243673390171E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"BMJicMhWiYCwWGY","DoubleRandomProvider":1.673721364173853E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"opJQ6rXVtue0Vy","DoubleRandomProvider":1.072555967109656E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"ZJoOi0SA63ZmNJ4","DoubleRandomProvider":1.7047601094399295E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"HW3SybVh3T0","DoubleRandomProvider":6.720467416186199E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"GHFUzT5Kx1wu","DoubleRandomProvider":7.652358130215527E307}

cat /tmp/mutli/example_30.json
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"6sYidNh5j2","DoubleRandomProvider":1.4310106954702759E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"xxSGSRYzrr","DoubleRandomProvider":3.114705607421097E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"bTRRFYzXmrldyV","DoubleRandomProvider":1.766301555724653E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"xEhBEaB5dvGcvx","DoubleRandomProvider":8.53456487152428E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"eKKM2pelcW","DoubleRandomProvider":7.076933646215422E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"SIQetc70Fg4PP","DoubleRandomProvider":8.891617649489153E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"PwCGrQmzuzE","DoubleRandomProvider":1.1810877488893049E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"Pyr0Ym15zfATry","DoubleRandomProvider":1.6726604422996763E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"jFgICf3f2B45FS","DoubleRandomProvider":1.1152019996510786E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"987OV2rbBVqpfh","DoubleRandomProvider":1.0117032096235709E308}
```

## 📐 Architecture
![Duzzy architecture](docs/images/duzzy_main.png)

## 🧬 Components

### Duzzy config
A `DuzzyConfig` is a yaml file that enables you to specify some configurations for `Duzzy` and composed to:
 - `rowKey`: the row key is an optional `field` that enables to specify a unique key for each row.  
 - `enricher` : an enricher is a component that enables field improvement by specifying which `provider` to use for generate data.
 - `sink` : a sink is a component that enables to specify where and in which format (via a `serializer`) data are written.

#### Field
A `Field` is composed of:
- `name`: the name of the field
- `type`: the type of the field
- `nullRate`: the rate of null values (0 means not null)
- `corruptedRate`: the rate of corrupted values (0 means no corrupted value), a corrupted value is a value that not match field constraint or field type (only if sink hasn't schema)
- `providers`: a list of providers used to generate data for the `Field`

#### Enricher
An `enricher` is a component that enables field improvement by specifying which `provider` to use for generate data.  
An `enricher` is composed of:
 - `query selector`: a query selector is a component that enables to specify which field to improve and it is formatted like that `key=value`. For example, to select a field named `city`, you have to use `name=city`. Another example, to select all fields of type `STRING`, you have to use `type=STRING`.
 - `provider identidier`: a provider identifier corresponds to the fully qualified name of the provider to use to improve the field. For example, to use a `AlphanumericRandomProvider`, you have to use `io.duzzy.plugin.provider.random.AlphanumericRandomProvider`.
 - `provider parameters`: a provider parameters is a map of parameters used to configure the provider. For example, to use a `AlphanumericRandomProvider` with a max length of 10, you have to use `max_length: 10`.

#### Provider
A `provider` is a component responsible for generating value. Data generated by a `provider` has always the same type but can be valid or corrupted.  
A `provider` can also be used into a `DuzzySchema` (see below).

#### Sink
A `sink` is a component that enables to specify where and in which format (via a `serializer`) data are written.  
Before writing data, a `sink` delegates data formatting to a `serializer`.

#### Serializer
A `serializer` is a component that enables to specify how data are formatted.

Below an example of `DuzzyConfig` to use a local file `sink` that will generate a local file name `/tmp/example.xml`:
```yaml 
---
row_key:
  name: key
  type: STRING
  null_rate: 0
  corrupted_rate: 0
  providers:
    - identifier: "io.duzzy.plugin.provider.random.AlphanumericRandomProvider"
enrichers:
  - query_selector: "name=city"
    provider_identifier: "io.duzzy.plugin.provider.random.AlphanumericRandomProvider"
    provider_parameters:
      min_length: 3
      max_length: 20
  - query_selector: "type=INTEGER"
    provider_identifier: "io.duzzy.plugin.provider.random.IntegerRandomProvider"
    provider_parameters:
      min: 1
      max: 9999
sink:
  identifier: "io.duzzy.plugin.sink.LocalFileSink"
  filename: "/tmp/example.xml"
  serializer:
    identifier: "io.duzzy.plugin.serializer.XmlSerializer"
    root_tag: myRootExample
    row_tag: myRowExample
```

### Duzzy schema
A `DuzzySchema` is a yaml file that enables you to specify the schema for generated data.   
A `DuzzySchema` is a list of `Field`. 

#### Field
A `Field` is composed of:
 - `name`: the name of the field
 - `type`: the type of the field
 - `nullRate`: the rate of null values (0 means not null)
 - `corruptedRate`: the rate of corrupted values (0 means no corrupted value), a corrupted value is a value that not match field constraint or field type (only if sink hasn't schema)
 - `providers`: a list of providers used to generate data for the `Field`

#### Provider
A `provider` is a component responsible for generating value. Data generated by a `provider` has always the same type but can be valid or corrupted.  
A `provider` can also be used into a `DuzzyConfig` (see above).

Below an example of `DuzzySchema`:
```yaml
---
fields:
  - name: stringConstant
    type: STRING
    null_rate: 0.5
    corrupted_rate: 0.5
    providers:
      - identifier: "io.duzzy.plugin.provider.constant.StringConstantProvider"
        value: myConstant
  - name: stringListConstant
    type: STRING
    null_rate: 0.5
    corrupted_rate: 0.5
    providers:
      - identifier: "io.duzzy.plugin.provider.constant.StringListConstantProvider"
        values: [ "one", "two", "three" ]
  - name: stringWeightedListConstant
    type: STRING
    null_rate: 0.5
    corrupted_rate: 0.5
    providers:
      - identifier: "io.duzzy.plugin.provider.constant.StringWeightedListConstantProvider"
        values:
          - value: first
            weight: 1
          - value: second
            weight: 2
          - value: third
            weight: 3
  - name: longIncrement
    type: LONG
    null_rate: 0.5
    corrupted_rate: 0.5
    providers:
      - identifier: "io.duzzy.plugin.provider.increment.LongIncrementProvider"
        start: 100
        step: 10
  - name: integerRandom
    type: INTEGER
    null_rate: 0.5
    corrupted_rate: 0.5
    providers:
      - identifier: "io.duzzy.plugin.provider.random.IntegerRandomProvider"
        min: 50
        max: 100
```

### Core components
The list of all core components is available [here](docs/core_components.md).  

### Core plugins
Some core plugins are available to extend Duzzy:  
[plugin-arrow](docs/plugin_arrow.md)  
[plugin-avro](docs/plugin_avro.md)  
[plugin-azure](docs/plugin_azure.md)  
[plugin-duckdb](docs/plugin_duckdb.md)  
[plugin-gcp](docs/plugin_gcp.md)  
[plugin-hdfs](docs/plugin_hdfs.md)  
[plugin-kafka](docs/plugin_kafka.md)  
[plugin-mysql](docs/plugin_mysql.md)  
[plugin-parquet](docs/plugin_parquet.md)  
[plugin-postgresql](docs/plugin_postgresql.md)

To install a core plugin, you can use the command:
```
DUZZY_VERSION=0.0.0
PLUGIN_ARTIFACT_ID=plugin-avro
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/${PLUGIN_ARTIFACT_ID}-${DUZZY_VERSION}-all.jar"
```

## 🤝 Contributing
Contributions are welcome! Feel free to report issues, submit a pull request or improve documentation.

## ⚖ Licence
Distributed under the Apache 2.0 License.