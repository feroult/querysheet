require 'test_helper'

class QSTest < Test::Unit::TestCase
  
  include Rack::Test::Methods

  def app
    QS
  end
    
  def test_query_api_setup    
    QS.query_api(:api => '/xpto', :query => 'select xpto')
          
    get '/qs/xpto'
    assert last_response.ok?
    assert_equal 'xpto', last_response.body
  end
  
end