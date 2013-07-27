ENV['RACK_ENV'] = 'test'
  
require 'test/unit'
require 'rack/test'

require 'sinatra'
require 'pg'

Dir["./lib/*.rb"].each { |file| require file }
