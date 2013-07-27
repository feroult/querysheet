watch( 'test/.*\.rb' ) do |md|
  puts "\n--------------------------------------------------------------------" 
  system("ruby -I test #{md[0]}")
end

watch( 'lib/(.*)\.rb' ) do |md| 
  puts "\n--------------------------------------------------------------------"
  Dir["test/unit/*.rb"].each { |file| system("ruby -I test #{file}") }  
  Dir["test/integration/*.rb"].each { |file| system("ruby -I test #{file}") }
end

