# MyStore API

[![OSCS Status](https://www.oscs1024.com/platform/badge/LeanneTN/mystore-api-dev.svg?size=small)](https://www.oscs1024.com/project/LeanneTN/mystore-api-dev?ref=badge_small)

## Introduction
MyStore is an online shopping website that build for getting familiar with
the way to build a software with more efficient and actual way. It also becomes
a project for me to write down the notes while I meet something new or meaningful
during the development.

MyStore API is the back-end of the whole project, includes the functions that 
handle the requirement from client-end or management-end that will be 
developed in the following period.

API documents have been released in WIKI. It was seperated by categories and functions it takes.

## Third party services
- Aliyun OSS
- Aliyun ECS
- Caffeine
- Mybatis Plus

## Packages not included in Maven
There are few packages which aren't using maven to manage
### Alipay Third party dependency
alipay-sdk-java-3.3.0.jar

alipay-sdk-java-3.3.0-source.jar

alipay-trade-sdk-20161215.jar

alipay-trade-sdk-20161215-source.jar

## Coding Style
In this project, my coding style is based on restful API and 
Spring Boot way to program

Beans need to be autowired are defined in utils packages

Some basic properties are written in application.properties. Most
properties inside are just examples, which is what I used during 
development. So most of them are expired or not fit in your computer.
