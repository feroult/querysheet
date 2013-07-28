require 'test_helper'

class SetupTest < Test::Unit::TestCase
  
  def test_connect 
    conn = PGHelper.connect
    
    conn.exec( "SELECT now() as now" ) do |result|
      result.each do |row|
        assert_not_nil row.values_at('now')
      end
    end
  end

  def test_query_as_json
    result = PGHelper.query_as_json("select 'xpto' as value")    
    assert_equal '[{"value":"xpto"}]', result
  end
  
end