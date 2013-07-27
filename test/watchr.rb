watch( 'test/.*\.rb' ) do |md|
  puts "------------------" 
  system("ruby -I test #{md[0]}")
end

watch( 'lib/(.*)\.rb' ) do |md| 
  puts "------------------"
  Dir["test/integration/*.rb"].each { |file| system("ruby -I test #{file}") }  
end

