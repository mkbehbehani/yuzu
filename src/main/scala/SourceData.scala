package yuzu

import scala.io.Source

object SourceData {
 val firstNamesFile = getClass.getResourceAsStream("/first_names_census.txt")
 val firstNames =  Source.fromInputStream(firstNamesFile).getLines.toArray
 val lastNamesFile = getClass.getResourceAsStream("/census_surnames_88K.txt")
 val lastNames = Source.fromInputStream(lastNamesFile).getLines.toArray
 val streetsFile = getClass.getResourceAsStream("/streets.txt")
 val streets = Source.fromInputStream(streetsFile).getLines.toArray
 val citiesFile = getClass.getResourceAsStream("/cities.txt")
 val cities = Source.fromInputStream(citiesFile).getLines.toArray
 val statesFile = getClass.getResourceAsStream("/states.txt")
 val states = Source.fromInputStream(statesFile).getLines.toArray
 val zipCodesFile = getClass.getResourceAsStream("/zip_codes.txt")
 val zipCodes = Source.fromInputStream(zipCodesFile).getLines.toArray
 val emailDomainsFile = getClass.getResourceAsStream("/email_domains.txt")
 val emailDomains = Source.fromInputStream(emailDomainsFile).getLines.toArray
}
