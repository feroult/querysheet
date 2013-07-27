watch( 'test/.*\.rb' ) do |md| 
  system("ruby -I test #{md[0]}") 
end

watch( 'lib/(.*)\.rb' ) do |md| 
  Dir["test/unit/*.rb"].each { |file| system("ruby -I test #{file}") }  
end

