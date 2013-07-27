require 'test_helper'

class APITest < Test::Unit::TestCase
  
  include Rack::Test::Methods

  def app
    QSSinatra
  end
     
  def setup
    @qs = QS.new
    QSSinatra.qs = @qs
  end
      
  def test_query_api_not_found
    get '/qs/group/x'    
    assert !last_response.ok?
    assert '', last_response.body    
  end
  
  def test_query_api_result
    @qs.query_api(:api => 'group/x', :query => "select 'xxx' as name, 10 as age")
    get '/qs/group/x'    

    assert last_response.ok?
    assert_equal "application/json;charset=utf-8", last_response.content_type    
    assert_equal '[{"name":"xxx","age":"10"}]', last_response.body
  end
end
