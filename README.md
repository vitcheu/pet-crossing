# Microservices Application of Pets Collecting, Nurturing and Battling

<details open="open">
	<ul>
		<li><a href="#overview">Overview</a></li>
		<li><a href="#supporting-services">Supporting Services</a></li>
        <li><a href="#technology-stack">Technology Stack</a></li>
        <li><a href="#implementation-details">Implementation Details</a></li>
     </ul>
</details>

## Overview
This application (**Pet Crossing**) provides backend API for a pet nurturing game. Built with **Spring Boot**, **Spring Cloud**, **Spring Data Jpa** and protected by **Spring Security**, it includes but not limited the following features:

 - User registration, login, and logout.
 - Rate Limiting
 - Accessing User profile.
 - Adding and removing pets.
 - Using items for pets.
 - Buying items in the store.
 - Playing with pets.
 - Pets battling based on turn.

## Supporting Services:
The folling are services that support the above functionalities, some of which can be scaled to have mutiple instances at the same time:

 - Discovery Server
 - Configuration Server
 - Gateway
 - Authentication Server
 - Administraition Dashboard
 - Tracing Server
 - Grafana Dashboard
 - Prometheus
 - Owner Service
 - Prop Service
 - Pet Service

Additionally, data persistence servers also exist:
  
 - Mysql Server
 - Redis Server

## Technology Stack

| technology                 | description          |
| -------------------------- | -------------------- |
| core framework             | spring boot3         |
| security framework         | spring security, jwt |
| microservices framword     | spring cloud         |
| persistent layer framework | spring data jpa,     |
| database                   | mysql                |
| cache                      | spring cache, redis  |
| rate limiting              | Bucket4j             |

## Implementation Details

| functionality              | how to implement                |
| -------------------------- | ------------------------------- |
| email verification         | jmx                             |
| sensitive data storing     | dotenv file and keystore        |
| transaction management     | declaratively use aop and proxy |
| taking effect of props     | event publification mechanism   |
| handling turn-based combat | websocket and juc               |
| cross-cutting consern      | spring cloud gateway            |