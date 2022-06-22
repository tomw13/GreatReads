package models

// code that generates the tables file in models
object CodeGen extends App {
  slick.codegen.SourceCodeGenerator.run(
    "slick.jdbc.PostgresProfile",
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/library?user=tom&password=secret",
    "/Users/tomw/Documents/Scala/Playground/tom/app/",
    "models", None, None, true, false
  )

}
