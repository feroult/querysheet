require 'test_helper'

class QSMock < QS
  def execute_query(api)
    api
  end  
end

class QSTest < Test::Unit::TestCase
  
  include Rack::Test::Methods

  def app
    QSSinatra
  end
     
  def setup
    @qs = QSMock.new
    QSSinatra.qs = @qs    
  end
      
  def test_query_api_setup
    @qs.query_api(:api => '/group/x', :query => '')
    
    get '/qs/xpto/x'
    assert last_response.ok?
    assert_equal 'xpto/x', last_response.body
  end
  
#  def test_simple_query
#    QS.query_api(:api => '/group/x', :query => '')
#  end
end