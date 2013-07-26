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

end