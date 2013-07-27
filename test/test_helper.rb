ENV['RACK_ENV'] = 'test'
  
require 'test/unit'
require 'rack/test'

require 'sinatra'
require 'pg'
require 'json'

Dir["./lib/*.rb"].each { |file| require file }
