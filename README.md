# Duzzy
Give me your schema, I'll give you your test data.

![Duzzy check](https://github.com/bhagenbourger/duzzy/actions/workflows/check.yml/badge.svg)
![GitHub License](https://img.shields.io/github/license/bhagenbourger/duzzy?color=%234169E1)

Duzzy is a data generation tool that aims to make generating test data easier.  
Duzzy wants to be as simple as possible, be able to run without any parameters (meaning every parameter has de  fault value) but fully configurable and extendable : easy start, easy tuning. 

## 🤔 Why Duzzy?
"Do you have a dataset to test this pipeline?"  
"I need data to test my job."  
"I can't reproduce this bug without data."  
"Do you know how to publish relevant message into this Kafka topic?"  
"Do you have a parquet file to test that case?"  

Did you already hear these sentences or maybe, did you already say that?  

Generate data shouldn't be a complicated, time-consuming or boring task.  
Duzzy wants to solve these problems and provide a no-brainer way to generate data for your need.  
So know, when you need data: duzzy it!  

## 🚀 Getting started
♨️ Duzzy is built with Java 21, so you need java 21 to run it. ♨️

Install latest version:
```
DUZZY_VERSION=0.0.0
wget "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/duzzy-${DUZZY_VERSION}.zip"
unzip duzzy-${DUZZY_VERSION}.zip
```

Run it:
```
"duzzy-${DUZZY_VERSION}/bin/duzzy"
```
Output:
```
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"MlPmlTBiF2CLP","DoubleRandomProvider":8.253334924246401E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"Oi8hyywQHTzL","DoubleRandomProvider":1.692914720729387E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"cI9PmIuBGlqLJ2","DoubleRandomProvider":1.2628298023208332E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"9NGrpMPDoT","DoubleRandomProvider":1.507758284837767E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"RP2RAqZqnzE","DoubleRandomProvider":1.7268510135708793E308}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"YGzKyAjT0kYk","DoubleRandomProvider":1.5748621166521904E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"XZX7oo8caGdT","DoubleRandomProvider":4.939056700087273E306}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"PNboqE54y9SAr","DoubleRandomProvider":8.683934011967188E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"3vK1t5MgRF","DoubleRandomProvider":7.672458763020226E306}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"NDAAkrF4cxPrqWr","DoubleRandomProvider":6.708664661592505E307}
DuzzyResult[duration=PT0.046S, rows=10, seed=2639303047660090771]
```

Into the next examples, `"duzzy-${DUZZY_VERSION}/bin/duzzy"` will be replace by `duzzy`.  
Print help:
```
duzzy -h
```
Output:
```
Usage: duzzy [-hV] [-c=File] [-f=File] [-o=OutputFormat] [-p=Class] [-r=Long]
             [-s=Long]
  -c, --config-file=File   Config file used to enrich the schema
  -f, --schema-file=File   Schema source file
  -h, --help               Show this help message and exit.
  -o, --output=OutputFormat
                           Output format, supported values: RAW, TXT, JSON,
                             XML, YAML
  -p, --schema-parser=Class
                           Qualified name of the parser class used to parse
                             schema file
  -r, --rows=Long          Number of rows to generate
  -s, --seed=Long          Seed used to generate
  -V, --version            Print version information and exit.
```

Set number of rows to generate:
```
duzzy -r 5
```
Output:
```
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"1mtRIZDug7wYV","DoubleRandomProvider":8.547228159771215E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"CtFjZVgNJrN2unM","DoubleRandomProvider":1.6531766170784796E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"E4562VNY0ohUF","DoubleRandomProvider":4.2061609883509E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"AKmlPZbUj7k","DoubleRandomProvider":4.623123394107451E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"BKWKIv6TVsc","DoubleRandomProvider":1.1892869523667485E308}
DuzzyResult[duration=PT0.048S, rows=5, seed=7513394063918507726]
```

Use seed to have idempotent result:
```
duzzy -s 1234
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
DuzzyResult[duration=PT0.046S, rows=10, seed=1234]
```

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
duzzy -c duzzy-config.yaml
```
Output:
```
DuzzyResult[duration=PT0.023S, rows=10, seed=-159643380333419490]
```

Check `/tmp/example.json  ` file content.
```
cat /tmp/example.json  
```
Output:
```                         
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"cqVk0FrkQrrH4o","DoubleRandomProvider":1.713387646957952E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"b7wuRi76PgEPY","DoubleRandomProvider":4.0183644768130334E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"LAGEIB5GmxK6ns0","DoubleRandomProvider":8.002403781016993E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"jjzULp1gWW9YxVx","DoubleRandomProvider":4.1304014904035556E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"saNdAuxEqOMIq","DoubleRandomProvider":4.2601590222894016E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"OjcDIsoaZRIx","DoubleRandomProvider":2.8638020021244796E307}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"Bm8E6JbLdiNV61A","DoubleRandomProvider":1.3392477688899436E308}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"ebWU72pAKU2pL","DoubleRandomProvider":7.553888472798153E306}
{"BooleanRandomProvider":true,"AlphanumericRandomProvider":"RDqihdkzgnr","DoubleRandomProvider":1.5325798180501981E307}
{"BooleanRandomProvider":false,"AlphanumericRandomProvider":"zsTDageA7f4Mvp","DoubleRandomProvider":1.7103441809149604E308}%
```

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
duzzy -c duzzy-config.yaml
```
Output:
```
<?xml version='1.0' encoding='UTF-8'?><rows><row><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>INDbx5EhMYT</AlphanumericRandomProvider><DoubleRandomProvider>1.2588448979350714E308</DoubleRandomProvider></row><row><BooleanRandomProvider>true</BooleanRandomProvider><AlphanumericRandomProvider>BgR2FWTpS2MWmQ</AlphanumericRandomProvider><DoubleRandomProvider>2.4051753657710203E307</DoubleRandomProvider></row><row><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>2yKHa4kx384deN</AlphanumericRandomProvider><DoubleRandomProvider>3.4878494076399964E306</DoubleRandomProvider></row><row><BooleanRandomProvider>true</BooleanRandomProvider><AlphanumericRandomProvider>eArY5y9ojdNvLd</AlphanumericRandomProvider><DoubleRandomProvider>8.529250011924854E307</DoubleRandomProvider></row><row><BooleanRandomProvider>true</BooleanRandomProvider><AlphanumericRandomProvider>KlknLlRKEmIIvf</AlphanumericRandomProvider><DoubleRandomProvider>9.566748145915126E307</DoubleRandomProvider></row><row><BooleanRandomProvider>true</BooleanRandomProvider><AlphanumericRandomProvider>daaVvkerY963yJ</AlphanumericRandomProvider><DoubleRandomProvider>9.267243778812058E306</DoubleRandomProvider></row><row><BooleanRandomProvider>true</BooleanRandomProvider><AlphanumericRandomProvider>heEVv3zup40</AlphanumericRandomProvider><DoubleRandomProvider>1.1738982952017788E308</DoubleRandomProvider></row><row><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>cttfg0Q3lxUin8</AlphanumericRandomProvider><DoubleRandomProvider>7.839907147050345E307</DoubleRandomProvider></row><row><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>SdKzH1RRfU</AlphanumericRandomProvider><DoubleRandomProvider>6.817546621150012E307</DoubleRandomProvider></row><row><BooleanRandomProvider>false</BooleanRandomProvider><AlphanumericRandomProvider>xg0BLq97mMXERq</AlphanumericRandomProvider><DoubleRandomProvider>7.760943165693689E307</DoubleRandomProvider></row></rows>
DuzzyResult[duration=PT0.043S, rows=10, seed=-6676110200100127519]
```

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
duzzy -f duzzy-schema.yaml
```
Output:
```
{"id":1,"name":"Epdg6I3M2y","gender":"F"}
{"id":2,"name":"QUlaDywiQsDnvmG","gender":"M"}
{"id":3,"name":"Zw0w9kz4BdGi8u","gender":"F"}
{"id":4,"name":"Dxbo9lClQ2W1jQ","gender":"F"}
{"id":5,"name":"jgunMtTM2OSC9AH","gender":"M"}
{"id":6,"name":"H1XWQkJ9UEc3","gender":"F"}
{"id":7,"name":"yXnK5eFctCQuV","gender":"F"}
{"id":8,"name":"nybs3GD61avuR4","gender":"F"}
{"id":9,"name":"BCRrEkckINe2W","gender":"M"}
{"id":10,"name":"CCyC69Adnjem","gender":"F"}
DuzzyResult[duration=PT0.019S, rows=10, seed=4511034117894600184]
```

Example using several options:
```
duzzy -f duzzy-schema.yaml -c duzzy-config.yaml -s 1234 -r 5 -o TXT 
```
Output:
```
<?xml version='1.0' encoding='UTF-8'?><rows><row><id>1</id><name>i7M95YxRzOB</name><gender>M</gender></row><row><id>2</id><name>ZxIJnpkufuy</name><gender>M</gender></row><row><id>3</id><name>DNir5lwHHUkC</name><gender>M</gender></row><row><id>4</id><name>mTykw0HhqM1b</name><gender>F</gender></row><row><id>5</id><name>3bacagIKM760</name><gender>F</gender></row></rows>
Duzzy generated 5 rows in PT0.035S with seed 1234
```

## 🧬 Components

Duzzy config  
=> Enricher  
=> Sink  
=> Serializer  

Duzzy schema  
=> Field   
=> Provider

## 🤝 Contributing
Contributions are welcome! Feel free to report issues, submit a pull request or improve documentation.

## ⚖ Licence
Distributed under the Apache 2.0 License.